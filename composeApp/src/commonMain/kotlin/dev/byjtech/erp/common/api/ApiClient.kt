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
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.*
import io.ktor.client.call.body
import io.ktor.client.plugins.api.createClientPlugin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ApiClient(
    engine: HttpClientEngine,
    val baseUrl: String,
    val basePort: Int,
    settings: Settings,
    private val dispatcher: CoroutineDispatcher,
    var onNavigationRequired: (SessionNavigationTarget) -> Unit
) {
    //esta aqui para agilisar el manejo de sesiones
    val sessionKey = "session_active"
    private var authHeaderProvider: (() -> String?)? = null
    val cookiesStorage = SettingsCookieStorage(settings)

    private val clientKtor = HttpClient(engine) {
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
        install(AuthorizationPlugin { settings.getStringOrNull(sessionKey) })
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
                    // Lanzar una excepción específica para sesión inválida
                    // AQUI PEDIRLE AL SESSION MANAGER QUE INVALIDE LA SESSION ACTUAL Y REDIRECCIONE AL LOGIN O SPLASH
                    //throw InvalidSessionException("Session is invalid or expired")
                    withContext(Dispatchers.Main){
                        //usa la clase del sessionmanager para manejar la navegacino en este caso
                        onNavigationRequired(SessionNavigationTarget.Splash)
                    }
                }
                // Para otros errores, usar el comportamiento por defecto
                else if (response.status.value >= 300) {
                    val clientException = ClientRequestException(response, response.bodyAsText())
                    throw clientException
                }
                //TODO???: agregar uno para Forbidden (no tiene los permisos necesarios)
            }
        }
    }

    private val ktorfit = Ktorfit.Builder()
        //.baseUrl("http://$baseUrl:$basePort")
        .httpClient(clientKtor)
        .build()

    /*
    se usa ktorfit.create aunque este deprecado por que la version recomendada tiene problemas con KMP
    cuando esos problemas se arreglen, se cambiara de:
    ktorfit.create<CoreAuth>() -> ktorfit.createCoreAuth()
     */

    //core
    val coreAuth = ktorfit.create<CoreAuth>() //importante no moverlo
    val coreApi = ktorfit.create<CoreApi>() //importante no moverlo

    //TODO() anidarlos de mejor forma, como : apiClient.roles.permission.getPermissions()
    //core-users
    val usersSuperAdminApi = ktorfit.create<UsersSuperAdminApi>()
    val usersTenantApi = ktorfit.create<UsersTenantApi>()

    //TODO(): crear los apis de los modulos
    //announcements-v1
    //val announcementsV1TenantApi = ktorfit.create<AnnouncementsV1TenantApi>()
    //val announcementsV1SuperAdminApi = ktorfit.create<AnnouncementsV1SuperAdminApi>()



    fun setAuthHeaderProvider(provider: (() -> String?)?) {
        this.authHeaderProvider = provider
    }


    //mover estas dos funciones a CoreApi
//    suspend fun test(): HttpResponse {
//        return clientKtor.get("/test")
//    }
//    suspend fun logout(): HttpResponse {
//        return clientKtor.get("/logout")
//    }


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

}

class InvalidSessionException(message: String) : Exception(message)

fun AuthorizationPlugin(tokenProvider: () -> String?) = createClientPlugin("AuthorizationPlugin") {
    onRequest { request, _ ->
        tokenProvider()?.let { token ->
            request.headers.append("Authentication", "Bearer $token")
        }
    }
}