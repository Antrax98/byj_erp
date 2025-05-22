package dev.byjtech.erp.common.session

import com.russhwolf.settings.Settings
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.api.InvalidSessionException
import dev.byjtech.erp.core.dto.ModuleDTO
import dev.byjtech.erp.core.dto.PermissionDTO
import dev.byjtech.erp.core.session.AppSession
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

// USARLO EN LA APP
//// En tu clase de Activity, Fragment, o donde manejes la navegación:
//class MyNavigationDelegate(private val navController: androidx.navigation.NavController) : NavigationDelegate {
//    override fun navigateToLogin() {
//        navController.navigate("login_route") // Reemplaza con tu ruta de login
//    }
//}

sealed class SessionNavigationTarget {
    object Login : SessionNavigationTarget()
    object Home : SessionNavigationTarget()
    object SuperHome : SessionNavigationTarget()
    object Splash : SessionNavigationTarget()
}

class SessionManager(
    private val apiClient: ApiClient,
    private val settings: Settings,
    var loginHandler: GoogleLoginHandler?,
    var onNavigationRequired: (SessionNavigationTarget) -> Unit,
) {

    private val _allowedModules = MutableStateFlow<List<ModuleDTO>>(emptyList())
    val allowedModules: StateFlow<List<ModuleDTO>> = _allowedModules.asStateFlow()

    private val _userPermissions = MutableStateFlow<List<PermissionDTO>>(emptyList())
    val userPermissions: StateFlow<List<PermissionDTO>> = _userPermissions.asStateFlow()

    //este es util solo si el usuario es un admin de la empresa, podria eliminarse
    private val _contractedModules = MutableStateFlow<List<ModuleDTO>>(emptyList())
    val contractedModules: StateFlow<List<ModuleDTO>> = _contractedModules.asStateFlow()

    suspend fun loadSession() {
        val sessionActive = isSessionActive()
        if (sessionActive) {
            println("Session active, fetching data.")
            try {
                val myType = apiClient.coreApi.getMyType()
                if (myType == "superadmin") {
                    onNavigationRequired(SessionNavigationTarget.SuperHome)
                } else if (myType == "tenant") {
                    //updateAllowedModules()
                    //updatePermissions()
                    //updateContractedModules()
                    onNavigationRequired(SessionNavigationTarget.Home)
                }
            } catch (e: InvalidSessionException) {
                // Si la sesión es inválida
                println("Session not active, by the server, handling invalid session.")
                handleInvalidSession()
                onNavigationRequired(SessionNavigationTarget.Login)
                return
            }
        } else {
            println("Session not active, handling invalid session.")
            handleInvalidSession()
            onNavigationRequired(SessionNavigationTarget.Login)
        }
    }


    fun setAppSession(appSession: AppSession) {
        try {
            println("Setting app session: $appSession")
            settings.putString(apiClient.sessionKey, appSession.toEncoded())
            settings.getString(apiClient.sessionKey,"")
            println("Storing Cookie")
            apiClient.storeCookie(appSession)
            println("Cookie stored")
            apiClient.setAuthHeaderProvider { appSession.toEncoded() }
        } catch (e: Exception) {
            println("Error al guardar la sesión: ${e.message}")
        } finally {
            println("Navigating to Home from setAppSession...")
            onNavigationRequired(SessionNavigationTarget.Splash)
        }
    }
    private fun getAppSession(): AppSession? {
        val encodedSession = settings.getString(apiClient.sessionKey, "")
        println("encodedSession: $encodedSession")
        return if (encodedSession.isNotEmpty()) {
            try {
                AppSession.fromEncoded(encodedSession)
            } catch (e: IllegalArgumentException) {
                println("Error al decodificar la sesión: ${e.message}")
                null
            }
        } else {
            null
        }
    }


    suspend fun updateAllowedModules() {
        try {
            val modules = apiClient.fetchAllowedModules()
            _allowedModules.value = modules
        } catch (e: InvalidSessionException) {
            // Si la sesión es inválida
            handleInvalidSession()
        } catch (e: Exception) {
            // Otros errores
            println("Error al obtener módulos: ${e.message}")
        }
    }

    suspend fun updatePermissions() {
        try {
            val permissions = apiClient.fetchUserPermissions()
            _userPermissions.value = permissions
        } catch (e: InvalidSessionException) {
            // Si la sesión es inválida
            handleInvalidSession()
        } catch (e: Exception) {
            // Otros errores
            println("Error al obtener permisos: ${e.message}")
        }
    }

    suspend fun updateContractedModules() {
        try {
            val contractedModules = apiClient.fetchContractedModules()
            _contractedModules.value = contractedModules
        } catch (e: InvalidSessionException) {
            // Si la sesión es inválida
            handleInvalidSession()
        } catch (e: Exception) {
            // Otros errores
            println("Error al obtener módulos contratados: ${e.message}")
        }
    }

    // Verifica si la sesión está activa
    //se podria modificar para que se lo pregunte a el servidor directamente...
    private suspend fun isSessionActive(): Boolean {
        val appSession : AppSession? = getAppSession()
        println("is session active??: ${appSession!=null}")
        return (appSession!=null)
    }

    //simplemente llama al handler del dispositivo para comensar el login
    fun initiateLogin() {
        loginHandler?.initiateGoogleLogin()
    }


    // Cierra la sesión
    suspend fun logout() {
        try {
            println("Logging out...")
            apiClient.coreAuth.logout()

        } catch (e: InvalidSessionException) {
            println("Logout failed: ${e.message}")
            // Si incluso el logout da un error de sesión inválida, manejarlo igual
            // (esto podría indicar que la sesión ya fue invalidada en el servidor)
        } catch (e: ClientRequestException){
            println("Logout failed: ${e.message}")
        }finally {
            println("Logout completed.")
            // Limpiar el estado local sin importar el resultado de la llamada al servidor
            try {
                handleInvalidSession()
            } catch (e: Exception) {
                println("Error al manejar la sesión inválida: ${e.message}")
            } finally {
                try {
                    withContext(Dispatchers.Main) {
                        println("Navigating to Login...")
                        onNavigationRequired(SessionNavigationTarget.Login)
                    }
                } catch (e: Exception) {
                    println("Error during navigation: ${e.message}")
                }
            }

        }
    }

    //divissar si esta funcion searada es realmente necesaria o se deja que logout() lo maneje directamente
    private suspend fun handleInvalidSession() {
        println("Session invalidated, clearing session data.")
        apiClient.clearCookies() //se limpian las cookies
        settings.remove(apiClient.sessionKey)
        if (getAppSession() == null) {
            println("Session cleared.")
        }
        //onNavigationRequired(SessionNavigationTarget.Login)
    }



}
