package dev.byjtech.erp.common.api

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import com.russhwolf.settings.*
import de.jensklingenberg.ktorfit.Ktorfit
import dev.byjtech.erp.core.api.CoreApi
import dev.byjtech.erp.core.api.CoreAuth
import dev.byjtech.erp.core.api.users.UsersSuperAdminApi
import dev.byjtech.erp.core.api.users.UsersTenantApi
import dev.byjtech.erp.core.dto.ModuleDTO
import dev.byjtech.erp.core.dto.PermissionDTO
import dev.byjtech.erp.core.session.AppSession
import dev.byjtech.erp.common.session.SessionNavigationTarget
import dev.byjtech.erp.common.session.SettingsCookieStorage
import dev.byjtech.erp.core.api.companies.CompaniesSuperAdminApi
import dev.byjtech.erp.core.dto.CompanyDTO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.*
import io.ktor.client.call.body
import io.ktor.client.plugins.api.createClientPlugin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.request.AssignPermissionRoleRequest
import dev.byjtech.erp.core.request.AssignRoleRequest
import dev.byjtech.erp.core.request.AssignSpecialPermissionRequest
import dev.byjtech.erp.core.request.CreateCompanyRequest
import dev.byjtech.erp.core.response.AccessibleModulesResponse
import dev.byjtech.erp.core.response.CompanyUsersResponse
import dev.byjtech.erp.core.response.ErrorList
import dev.byjtech.erp.core.response.PermissionKeysResponse
import dev.byjtech.erp.core.response.PermittedModulesResponse
import dev.byjtech.erp.core.response.RolePermisisonKeysResponse
import dev.byjtech.erp.core.response.SubscribedModulesResponse
import dev.byjtech.erp.core.response.SubscriptionsModResponse
import dev.byjtech.erp.core.response.UserPermissionsResponse
import dev.byjtech.erp.core.response.UserRolesResponse
import io.ktor.client.utils.EmptyContent.contentType
import java.util.UUID


class ApiClient(
    engine: HttpClientEngine,
    val baseUrl: String,
    val basePort: Int?,
    settings: Settings,
    private val dispatcher: CoroutineDispatcher,
    //var onNavigationRequired: (SessionNavigationTarget) -> Unit
) {

    private val _events = MutableSharedFlow<ApiEvent>()
    val events: SharedFlow<ApiEvent> = _events

    private suspend fun emitEvent(event: ApiEvent){
        _events.emit(event)
    }

    //esta aqui para agilisar el manejo de sesiones
    val sessionKey = "session_active"
    private var authHeaderProvider: (() -> String?)? = null
    private val cookiesStorage = SettingsCookieStorage(settings)

    //este tiene que ser publico para poder usarlo en las apis de los demas modulos
    val clientKtor = HttpClient(engine) {
        install(HttpCookies) {
            storage = cookiesStorage
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(authorizationPlugin { settings.getStringOrNull(sessionKey) })
        defaultRequest {
            url {
                //protocol = if (baseUrl.startsWith("https")) URLProtocol.HTTPS else URLProtocol.HTTP
                //protocol = URLProtocol.HTTPS
                println(baseUrl)
                protocol = URLProtocol.HTTPS
                host = baseUrl
                //basePort?.let { port = it }
            }
            // Añadir el appSession en el header de todas las peticiones
//            settings.getStringOrNull(sessionKey)?.let {
//                header("Authorization", "Bearer $it")
//            }

        }
        HttpResponseValidator {
            validateResponse { response ->
                if (response.status == HttpStatusCode.Unauthorized) {
                    //NO ENVIO TOKEN o FUE INVALIDADA LA SESSION EN EL SERVER
                    emitEvent(ApiEvent.Unauthorized)

                    // Lanzar una excepción específica para sesión inválida
                    // AQUI PEDIRLE AL SESSION MANAGER QUE INVALIDE LA SESSION ACTUAL Y REDIRECCIONE AL LOGIN O SPLASH

                }
                else if(response.status == HttpStatusCode.Forbidden){
                    //SIN ACCESO AL MODULO O PERMISOS INSUFICIENTES
                    emitEvent(ApiEvent.Forbidden)
                    //envia a la pantalla principal o a una con los permisos suficientes

                }
                // Para otros errores, usar el comportamiento por defecto
//                else if (response.status.value >= 300) {
//                    val clientException = ClientRequestException(response, response.bodyAsText())
//                    throw clientException
//                }

            }
        }
    }

    //!!!NO USAR KTORFIT!!!!
//    val ktorfit = Ktorfit.Builder()
//        .httpClient(clientKtor)
//        .build()

    //core
    val coreAuth = CoreAuth(clientKtor)//ktorfit.create<CoreAuth>() //importante no moverlo
    val coreApi = CoreApi(clientKtor)//ktorfit.create<CoreApi>() //importante no moverlo

    //TODO() QUITAR TODOS LOS KTORFIT y usar ktorClient directamente
    //core-users
    //val usersSuperAdminApi = ktorfit.create<UsersSuperAdminApi>()
    val usersTenantApi = UsersTenantApi(clientKtor)//ktorfit.create<UsersTenantApi>()

    //companies
    val companiesSA = CompanySA(clientKtor)

    //roles
    val rolesT = RoleT(clientKtor)

    //users
    val usersT = UsersT(clientKtor)

    //subscriptions
    val subscriptionsSA = SubscriptionsSA(clientKtor)


    fun setAuthHeaderProvider(provider: (() -> String?)?) {
        this.authHeaderProvider = provider
    }


    suspend fun clearCookies() {
        cookiesStorage.clearAll()
    }

    fun storeCookie(appSession: AppSession) {
        val cookie = Cookie(
            name = "user_session",  // Nombre de la cookie
            value = appSession.toEncoded(),  // Valor de la cookie (deberías tener una forma de serializar el AppSession)
            //maxAge = 3600,  // Tiempo de expiración en segundos
            domain = baseUrl,  // Dominio
            path = "/",  // Ruta
            secure = true,  // Usar HTTPS si es necesario, por ahora es false
            //httpOnly = true  // Solo accesible por HTTP, no JavaScript
        )

        CoroutineScope(dispatcher).launch {
            cookiesStorage.addCookie(baseUrl,cookie)
        }
    }


    //obtiene los permisos del usuario
    suspend fun fetchUserPermissions(): List<PermissionDTO> {
        return clientKtor.get("/permissions").body()
    }

    //obtiene los modulos permitidos para el usuario
    suspend fun fetchAllowedModules(): List<ModuleDTO> {
        return clientKtor.get("/modules/allowed").body()
    }

    //obtiene los modulos contratados por la empresa
    suspend fun fetchContractedModules(): List<ModuleDTO> {
        return clientKtor.get("/modules/contracted").body()
    }

    private fun authorizationPlugin(tokenProvider: () -> String?) = createClientPlugin("AuthorizationPlugin") {
        onRequest { request, _ ->
            tokenProvider()?.let { token ->
                request.headers.append("Authentication", "Bearer $token")
            }
        }
    }

}


//TODO: mover todo lo de abajo a sus propios archivos o algo
sealed class ApiEvent {
    data object Unauthorized : ApiEvent()
    data object Forbidden : ApiEvent()
}

class InvalidSessionException(message: String) : Exception(message)

class CompanySA(private val client: HttpClient) {

    suspend fun getAllCompanies(): Set<CompanyDTO> {
        return try {
            client.get("api/core/companies/super-admin/all-companies").body()
        } catch (e: Exception) {
            emptySet()
        }
    }
    //TODO(): usar ApiResponse en los que no lo usen
    suspend fun createCompany(data: CreateCompanyRequest): ApiResponse<Unit,ErrorList?> {
        return try {
            val response = client.post("api/core/companies/super-admin/create-company") {
                contentType(ContentType.Application.Json)
                setBody(data)
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    println("OK")
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.Conflict -> {
                    val requestBody = response.body<ErrorList>()
                    println("CONFLICT_REQUEST")
                    ApiResponse.Error(requestBody, "CONFLICT_REQUEST")
                }
                HttpStatusCode.BadRequest -> {
                    println("BAD_REQUEST")
                    ApiResponse.Error(null, "BAD_REQUEST")
                }
                else -> {
                    println("UNKNOWN_REQUEST_ERROR")
                    ApiResponse.Error(null, "UNKNOWN_REQUEST_ERROR")
                }
                //TODO(): agregar mas errores
            }
        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(null, "NETWORK_ERROR")
        }
    }

    suspend fun updateCompanyName(companyId: String, newName: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.patch("api/core/companies/super-admin/update-company-name/$companyId/$newName"){
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")

        }
    }

    suspend fun updateCompanyEmail(companyId: String, newEmail: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.patch("api/core/companies/super-admin/update-company-contact-email/$companyId/$newEmail"){
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

}

class RoleT(private val client: HttpClient) {
    suspend fun getRoleById(roleId: String): ApiResponse<RoleDTO, Unit?> {
        return try {
            val response = client.get("api/core/roles/tenant/$roleId")
            when (response.status) {
                HttpStatusCode.OK -> {
                    val role = response.body<RoleDTO>()
                    ApiResponse.Success(role)
                }
                HttpStatusCode.BadRequest -> {
                    ApiResponse.Error(null, "NO_ROLE_ID")
                }
                else -> {
                    ApiResponse.Error(null, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            ApiResponse.Error(null, "NETWORK_ERROR")
        }
    }

    suspend fun getAllRoles(): ApiResponse<Set<RoleDTO>, Unit?> {
        return try {
            val response = client.get("api/core/roles/tenant/all-roles")
            when (response.status) {
                HttpStatusCode.OK -> {
                    val roles = response.body<Set<RoleDTO>>()
                    ApiResponse.Success(roles)
                }
                HttpStatusCode.BadRequest -> {
                    ApiResponse.Error(null, "NO_COMPANY")
                }
                else -> {
                    ApiResponse.Error(null, "UNKNOWN_REQUEST_ERROR")
                }
            }

        } catch (e: Exception) {
            ApiResponse.Error(null, "NETWORK_ERROR")
        }
    }
    suspend fun assignRoleToUser(data: AssignRoleRequest): ApiResponse<Unit, Unit> {
        return try {
            val response = client.post("api/core/roles/tenant/assign-role-to-user") {
                contentType(ContentType.Application.Json)
                setBody(data)
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    ApiResponse.Error(Unit, "ERROR_ASSIGNING_ROLE")
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun getPermissionsByRoleId(roleId: String): ApiResponse<Set<PermissionWithKey>, Unit?> {
        return try {
            val response = client.get("api/core/roles/tenant/role-permissions/$roleId")
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<RolePermisisonKeysResponse>()
                    ApiResponse.Success(data.permissions)
                }
                HttpStatusCode.BadRequest -> {
                    ApiResponse.Error(null, "NO_ROLE_ID")
                }
                else -> {
                    ApiResponse.Error(null, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(null, "NETWORK_ERROR")
        }
    }

    suspend fun assignPermissionsToRole(data : AssignPermissionRoleRequest): ApiResponse<Unit, Unit> {
        return try {
            val response = client.post("api/core/roles/tenant/assign-permissions-to-role") {
                contentType(ContentType.Application.Json)
                setBody(data)
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    println(errorMessage)
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }

        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun createRole(data: RoleDTO): ApiResponse<Unit, Unit>{
        return try {
            val response = client.post("api/core/roles/tenant/create-role") {
                contentType(ContentType.Application.Json)
                setBody(data)
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }

                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }

                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun getModulesPermissionKeys(moduleIds: Set<String>): ApiResponse<Set<PermissionWithKey>, Unit?> {
        return try {
            val response = client.post("api/core/roles/tenant/modules-permissionkeys") {
                contentType(ContentType.Application.Json)
                setBody(moduleIds)
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    val permissions = response.body<Set<PermissionWithKey>>()
                    ApiResponse.Success(permissions)
                }
                else -> {
                    ApiResponse.Error(null, "UNKNOWN_REQUEST_ERROR")
                }
            }

        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(null, "NETWORK_ERROR")
        }
    }

    suspend fun deleteRolePermission(roleId: String, permissionId: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.delete("api/core/roles/tenant/delete-role-permission/$roleId/$permissionId"){
                expectSuccess = false
                contentType(ContentType.Application.Json)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun deleteUserPermission(userId: String, permissionId: String): ApiResponse<Unit, Unit> {
        return try {
            //TODO: mover ruta a users
            val response = client.delete("api/core/roles/tenant/delete-user-permission/$userId/$permissionId"){
                expectSuccess = false
                contentType(ContentType.Application.Json)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun deleteUserRole(userId: String, roleId: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.delete("api/core/roles/tenant/delete-user-role/$userId/$roleId") {
                expectSuccess = false
                contentType(ContentType.Application.Json)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun updateRoleName(roleId: String, newName: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.patch("api/core/roles/tenant/update-role-name/$roleId/$newName"){
                expectSuccess = false
                contentType(ContentType.Application.Json)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            return ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

}

class UsersT(private val client: HttpClient){
    suspend fun createUser(userDTO: UserDTO):ApiResponse<Unit, Unit> {
        return try {
            val response = client.post("api/core/users/tenant/create-user") {
                contentType(ContentType.Application.Json)
                setBody(userDTO)
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                    //
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Error){
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun getUserSpecialPermissionsWithKey(userId: String): ApiResponse<Set<PermissionWithKey>, Unit> {
        return try {
            val response = client.get("api/core/users/tenant/user-special-permissions/$userId")
            when (response.status) {
                HttpStatusCode.OK -> {
                    val permissions = response.body<Set<PermissionWithKey>>()
                    ApiResponse.Success(permissions)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception){
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun assignSpecialPermission(userId: String, permissionKey: PermissionKey): ApiResponse<Unit, Unit> {
        val data = AssignSpecialPermissionRequest(userId = userId, permissionId = null, permissionKey =  permissionKey)
        return try {
            val response = client.post("api/core/users/tenant/assign-special-permission") {
                contentType(ContentType.Application.Json)
                setBody(data)
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun updateUserName(userId: String, newName: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.patch("api/core/users/tenant/update-user-name/$userId/$newName"){
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }

                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }

                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
            } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }
}

class SubscriptionsSA(private val client: HttpClient) {
    suspend fun getCompanySubscriptions(companyId: String): ApiResponse<Set<SubscriptionsModResponse>, Unit> {
        return try{
            val response = client.get("api/core/subscriptions/super-admin/company-subscriptions/$companyId"){
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<Set<SubscriptionsModResponse>>()
                    ApiResponse.Success(data)
                }
                //"NO_COMPANY_ID"
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception){
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun getAllModules(): ApiResponse<Set<ModuleDTO>,Unit> {
        return try {
            val response = client.get("api/core/subscriptions/super-admin/possible-modules"){
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<Set<ModuleDTO>>()
                    ApiResponse.Success(data)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun createSubscription(companyId: String, moduleId: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.post("api/core/subscriptions/super-admin/assign-module-to-company/$companyId/$moduleId"){
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK ->
                    ApiResponse.Success(Unit)
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun changeSubscriptionAccess(subscriptionId: String, isAccessible: Boolean): ApiResponse<Unit, Unit>{
        return try {
            val response = client.patch("api/core/subscriptions/super-admin/update-access-status/$subscriptionId/$isAccessible"){
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK ->
                    ApiResponse.Success(Unit)
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }
}

//TODO: recrear con apiresponse (cambio de Ktorfit a raw ktor)
class CoreAuth(private val client: HttpClient) {
    suspend fun test(): String {
        return try {
            val response = client.get("auth/test")
            response.body()
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun logout(): String {
        return try {
            val response = client.get("auth/logout"){
                expectSuccess = false
            }
            response.body()
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun login(): String {
        return try {
            val response = client.get("auth/login"){
                expectSuccess = false
            }
            response.body()
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun getMyType(): String {
        return try {
            val response = client.get("auth/me/type"){
                expectSuccess = false
            }
            response.body()
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun getMe(): UserDTO {
        return try {
            val response = client.get("auth/me") {
                expectSuccess = false
            }
            response.body<UserDTO>()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun permittedModules(): PermittedModulesResponse {
        return try {
            val response = client.get("auth/permitted-modules") {
                expectSuccess = false
            }
            response.body<PermittedModulesResponse>()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun subscribedModules(): SubscribedModulesResponse {
        return try {
            val response = client.get("auth/subscribed-modules") {
                expectSuccess = false
            }
            response.body<SubscribedModulesResponse>()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun accessibleModules(): AccessibleModulesResponse {
        return try {
            val response = client.get("auth/accessible-modules") {
                expectSuccess = false
            }
            response.body<AccessibleModulesResponse>()
            } catch (e: Exception) {
            throw e
        }
    }

    suspend fun userPermissions(): UserPermissionsResponse {
        return try {
            val response = client.get("auth/user-permissions") {
                expectSuccess = false
            }
            response.body<UserPermissionsResponse>()
            } catch (e: Exception) {
            throw e
        }
    }

    suspend fun userRoles(userId: String): UserRolesResponse {
        return try {
            val response = client.get("auth/userRoles/$userId") {
                expectSuccess = false
            }
            response.body<UserRolesResponse>()
            } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getSpecialPermissionsByUserId(userId: String): PermissionKeysResponse {
        return try {
            val response = client.get("auth/userSpecialPermissions/$userId") {
                expectSuccess = false
            }
            response.body<PermissionKeysResponse>()
            } catch (e: Exception) {
            throw e
        }
    }

}

class CoreApi(private val client: HttpClient) {
    suspend fun getMyType(): String {
        return try {
            val response = client.get("api/core/users/me/type"){
                expectSuccess = false
            }
            response.body()
        } catch (e: Exception) {
            ""
        }
    }
}

class UsersTenantApi(private val client: HttpClient) {
    suspend fun getActualUser(): UserDTO {
        return try {
            val response = client.get("api/core/users/tenant/me") {
                expectSuccess = false
            }
            response.body<UserDTO>()
            } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getCompanyUsers(): CompanyUsersResponse? {
        return try {
            val response = client.get("api/core/users/tenant/company-users") {
                expectSuccess = false
            }
            response.body<CompanyUsersResponse>()
            } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getUser(userId: String): UserDTO? {
        return try {
            val response = client.get("api/core/users/tenant/$userId") {
                expectSuccess = false
            }
            response.body<UserDTO>()
            } catch (e: Exception) {
            throw e
        }
    }

    suspend fun assignRole(assignRoleRequest: AssignRoleRequest): ApiResponse<Unit,Unit> {
        return try {
            val response = client.post("api/core/users/assign-role") {
                contentType(ContentType.Application.Json)
                setBody(assignRoleRequest)
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }

                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }

                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun assignSpecialPermission(
        assignSpecialPermissionRequest: AssignSpecialPermissionRequest
    ): ApiResponse<Unit, Unit> {
        return try {
            val response = client.post("api/core/users/assign-special-permission") {
                contentType(ContentType.Application.Json)
                setBody(assignSpecialPermissionRequest)
            }
            when (response.status) {
                HttpStatusCode.OK -> ApiResponse.Success(Unit)
                HttpStatusCode.BadRequest -> ApiResponse.Error(Unit, response.bodyAsText())
                else -> ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun unassignRole(userId: String, roleId: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.delete("api/core/users/unassign-role/$userId/$roleId")
            when (response.status) {
                HttpStatusCode.OK -> ApiResponse.Success(Unit)
                HttpStatusCode.BadRequest -> ApiResponse.Error(Unit, response.bodyAsText())
                else -> ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun unassignSpecialPermission(userId: String, permissionId: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.delete("api/core/users/unassign-special-permission/$userId/$permissionId")
            when (response.status) {
                HttpStatusCode.OK -> ApiResponse.Success(Unit)
                HttpStatusCode.BadRequest -> ApiResponse.Error(Unit, response.bodyAsText())
                else -> ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun createUser(userDTO: UserDTO): ApiResponse<Unit, Unit> {
        return try {
            val response = client.post("api/core/users/create-user") {
                contentType(ContentType.Application.Json)
                setBody(userDTO)
            }
            when (response.status) {
                HttpStatusCode.OK -> ApiResponse.Success(Unit)
                HttpStatusCode.BadRequest -> ApiResponse.Error(Unit, response.bodyAsText())
                else -> ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

}