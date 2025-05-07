package dev.byjtech.erp.session

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.russhwolf.settings.Settings
import dev.byjtech.erp.core.session.AppSession
import dev.byjtech.erp.getOrCreateDeviceId
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.net.URI
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import io.ktor.server.plugins.cors.routing.*
import kotlinx.io.IOException
import java.util.Base64
import io.github.cdimascio.dotenv.dotenv



class DesktopGoogleLoginHandler(
    private val settings: Settings,
    private val onSessionReceived: (AppSession) -> Unit
) : GoogleLoginHandler {
    private var server: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>? = null
    private var job: Job? = null
    private var isServerRunning by mutableStateOf(false)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        startCallbackListener()
    }

    private val dotenv = dotenv {
        ignoreIfMissing = false
    }

    override fun initiateGoogleLogin() {
        println("Initiating Google login from Desktop")
        val deviceId = getOrCreateDeviceId(settings)
        val platform = "desktop"
        val stateMap = mapOf(
            "platform" to platform,
            "deviceId" to deviceId
        )
        val jsonState = Json.encodeToString(stateMap)
        val encodedState = Base64.getUrlEncoder().encodeToString(jsonState.toByteArray())
        //val loginUrl = "http://localhost:8080/auth/login?state=$encodedState"
        val loginUrl = "http://${dotenv["BASE_URL"]}:${dotenv["BASE_PORT"]}/auth/login?state=$encodedState"
        println("loginUrl: $loginUrl")
        openBrowser(loginUrl)
    }

    private fun startCallbackListener() {
        if (isServerRunning) {
            return
        }
        isServerRunning = true
        job = coroutineScope.launch {
            try {
                server = embeddedServer(Netty, port = 12345) {
                    install(CORS) {
                        anyHost()
                    }
                    routing {
                        get("/callback") {
                            val encoded = call.request.queryParameters["appSession"]
                            if (encoded != null) {
                                try {
                                    val json = String(Base64.getUrlDecoder().decode(encoded))
                                    val session = Json.decodeFromString<AppSession>(json)

                                    withContext(Dispatchers.Main) {
                                        onSessionReceived.invoke(session)
                                    }
                                    call.respondText("Inicio de sesión exitoso. Puedes cerrar esta ventana.")
                                } catch (e: Exception) {
                                    call.respondText("Error al procesar la sesión: ${e.message}")
                                }
                            } else {
                                call.respondText("No se encontró el parámetro 'appSession'.")
                            }
                        }
                    }
                }.start(wait = false)
            } catch (e: Exception) {
                println("Error starting server: ${e.message}")
                isServerRunning = false
            }
        }
    }
    fun stopServer() {
        coroutineScope.launch {
            server?.stop(1_000, 2_000)
            job?.cancelAndJoin()
        }
        isServerRunning = false
        job = null
    }
}

fun openBrowser(url: String) {
    val os = System.getProperty("os.name").lowercase()

    try {
        when {
            os.contains("win") || os.contains("mac") -> {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(URI(url))
                } else {
                    println("Desktop no soportado en este sistema.")
                }
            }
            os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
                ProcessBuilder("xdg-open", url).start()
            }
            else -> {
                println("Sistema operativo no reconocido: $os")
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: UnsupportedOperationException) {
        println("Operación no soportada en este sistema.")
        e.printStackTrace()
    }
}