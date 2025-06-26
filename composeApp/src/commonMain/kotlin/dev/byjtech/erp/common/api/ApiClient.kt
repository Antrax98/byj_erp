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
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.request.AssignPermissionRoleRequest
import dev.byjtech.erp.core.request.AssignRoleRequest
import dev.byjtech.erp.core.request.CreateCompanyRequest
import dev.byjtech.erp.core.response.ErrorList
import dev.byjtech.erp.core.response.RolePermisisonKeysResponse
import io.ktor.client.utils.EmptyContent.contentType
import java.util.UUID


class ApiClient(
    engine: HttpClientEngine,
    val baseUrl: String,
    val basePort: Int,
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
                protocol = URLProtocol.HTTP
                host = baseUrl
                port = basePort
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
    val ktorfit = Ktorfit.Builder()
        .httpClient(clientKtor)
        .build()

    //core
    val coreAuth = ktorfit.create<CoreAuth>() //importante no moverlo
    val coreApi = ktorfit.create<CoreApi>() //importante no moverlo

    //TODO() QUITAR TODOS LOS KTORFIT y usar ktorClient directamente
    //core-users
    val usersSuperAdminApi = ktorfit.create<UsersSuperAdminApi>()
    val usersTenantApi = ktorfit.create<UsersTenantApi>()

    //companies
    val companiesSA = CompanySA(clientKtor)

    //roles
    val rolesT = RoleT(clientKtor)


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
            secure = false,  // Usar HTTPS si es necesario, por ahora es false
            httpOnly = true  // Solo accesible por HTTP, no JavaScript
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
    //TODO(): usar ApiResponse en los que no lo usen
    suspend fun getAllCompanies(): Set<CompanyDTO> {
        return try {
            client.get("api/core/companies/super-admin/all-companies").body()
        } catch (e: Exception) {
            emptySet()
        }
    }

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

    suspend fun getPermissionsByRoleId(roleId: String): ApiResponse<RolePermisisonKeysResponse, Unit?> {
        return try {
            val response = client.get("api/core/roles/tenant/role-permissions/$roleId")
            when (response.status) {
                HttpStatusCode.OK -> {
                    val permissions = response.body<RolePermisisonKeysResponse>()
                    ApiResponse.Success(permissions)
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
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

}