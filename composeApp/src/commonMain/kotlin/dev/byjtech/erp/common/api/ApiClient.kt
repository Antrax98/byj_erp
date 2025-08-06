package dev.byjtech.erp.common.api

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import com.russhwolf.settings.*
import dev.byjtech.erp.core.dto.ModuleDTO
import dev.byjtech.erp.core.dto.PermissionDTO
import dev.byjtech.erp.core.session.AppSession
import dev.byjtech.erp.common.session.SettingsCookieStorage
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
import dev.byjtech.erp.core.api.CompanySuperAdmin
import dev.byjtech.erp.core.api.CoreApi
import dev.byjtech.erp.core.api.CoreAuth
import dev.byjtech.erp.core.api.RolesTenant
import dev.byjtech.erp.core.api.SubscriptionsSuperAdmin
import dev.byjtech.erp.core.api.UsersT
import dev.byjtech.erp.core.api.UsersTenant
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.request.AssignSpecialPermissionRequest
import dev.byjtech.erp.core.response.SubscriptionsModResponse


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
                protocol = URLProtocol.HTTP
                //protocol = URLProtocol.HTTPS
                host = baseUrl
                basePort?.let { port = it }
            }

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

            }
        }
    }

    //core
    val coreAuth = CoreAuth(clientKtor)
    val coreApi = CoreApi(clientKtor)

    //core-users
    val usersTenantApi = UsersTenant(clientKtor)

    //companies
    val companiesSA = CompanySuperAdmin(clientKtor)

    //roles
    val rolesT = RolesTenant(clientKtor)

    //users
    val usersT = UsersT(clientKtor)

    //subscriptions
    val subscriptionsSA = SubscriptionsSuperAdmin(clientKtor)


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