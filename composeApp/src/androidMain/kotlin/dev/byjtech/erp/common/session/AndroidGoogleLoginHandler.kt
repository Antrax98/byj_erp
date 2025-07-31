package dev.byjtech.erp.common.session

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import com.russhwolf.settings.Settings
import dev.byjtech.erp.AndroidEnv
import dev.byjtech.erp.common.session.GoogleLoginHandler
import dev.byjtech.erp.getOrCreateDeviceId
import kotlinx.serialization.json.Json
import java.util.Base64
import kotlin.text.isNullOrBlank


class AndroidGoogleLoginHandler(private val activity: ComponentActivity, private val settings: Settings) :
    GoogleLoginHandler {
    override fun initiateGoogleLogin() {
        AndroidEnv.init(activity)
        val baseUrlFromEnv = AndroidEnv.get("BASE_URL")
        val basePortFromEnv = AndroidEnv.get("BASE_PORT")
        val deviceId = getOrCreateDeviceId(settings)
        val platform = "android"
        val stateMap = mapOf(
            "platform" to platform,
            "deviceId" to deviceId
        )
        val jsonState = Json.encodeToString(stateMap)
        val encodedState = Base64.getUrlEncoder().encodeToString(jsonState.toByteArray())
        val hostPortPart = if (basePortFromEnv == "NO_PORT") {
            baseUrlFromEnv // Solo el host si el puerto es "NO_PORT"
        } else {
            "${baseUrlFromEnv}:${basePortFromEnv}" // host:puerto si el puerto existe
        }
        //val loginUrl = "http://proyectron.duckdns.org:8080/auth/login?state=$encodedState"
        val loginUrlString = "https://${hostPortPart}/auth/login?state=$encodedState"
        println("Android loginUrlString: $loginUrlString")
        //val loginUrl = "http://${baseUrl}:${basePort}/auth/login?state=$encodedState"
//        val intent = Intent(Intent.ACTION_VIEW, loginUrl.toUri())
//        activity.startActivity(intent)
        try {
            val intent = Intent(Intent.ACTION_VIEW, loginUrlString.toUri())
            activity.startActivity(intent)
        } catch (e: Exception) {
            println("Error al iniciar el intent de login: ${e.message}")
            e.printStackTrace()
        }
    }
}