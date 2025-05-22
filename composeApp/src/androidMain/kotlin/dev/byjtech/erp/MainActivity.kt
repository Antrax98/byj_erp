package dev.byjtech.erp


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import dev.byjtech.erp.core.moduleRoot.RootComponentImpl
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager
import io.ktor.client.engine.android.Android
import com.russhwolf.settings.Settings
import dev.byjtech.erp.common.session.provideSettings
import dev.byjtech.erp.core.session.AppSession
import dev.byjtech.erp.common.session.AndroidGoogleLoginHandler
import dev.byjtech.erp.common.session.initAndroidSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import java.util.Base64

class MainActivity : ComponentActivity() { //se cambia ComponentActivity por AppCompatActivity
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //androidContext = applicationContext //se crea el contexto
        initAndroidSettings(applicationContext)
        val settings : Settings = provideSettings()
        AndroidEnv.init(this)
        apiClient = ApiClient(
            engine = Android.create(),
            baseUrl = AndroidEnv.get("BASE_URL"),
            basePort = AndroidEnv.get("BASE_PORT").toInt(),
            settings = settings,
            dispatcher = Dispatchers.IO,
            onNavigationRequired = {}
        )

        val loginHandler = AndroidGoogleLoginHandler(this, settings)
        sessionManager = SessionManager(apiClient, settings,loginHandler) {}
        val root = RootComponentImpl(
            defaultComponentContext(), //se queda igual, ya que estamos en AppCompatActivity???? no estamos en AppCompat
            sessionManager,
            apiClient
        )
        setContent {
            App(root) //se llama a App()
        }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        intent.data?.let { uri ->
            if (uri.scheme == "erpapp" && uri.host == "callback") {
                val appSessionEncoded = uri.getQueryParameter("appSession")
                if (appSessionEncoded != null) {
                    try {
                        val decodedCookie = String(Base64.getUrlDecoder().decode(appSessionEncoded))
                        val appSession = Json.decodeFromString<AppSession>(decodedCookie)
                        // Guardar la appSession en el SessionManager
                        sessionManager.setAppSession(appSession)
                        Log.d("LoginCallback", "AppSession decodificada y guardada: $appSession")
                    } catch (e: Exception) {
                        Log.e("LoginCallback", "Error al procesar la cookie: ${e.message}")
                    }
                } else {
                    Log.e("LoginCallback", "Parámetro 'appSession' no encontrado en la URL.")
                }
            }
        }
    }

}

