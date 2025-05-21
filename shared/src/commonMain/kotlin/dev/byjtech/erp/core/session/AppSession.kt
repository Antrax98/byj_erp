package dev.byjtech.erp.core.session

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.text.toByteArray
import java.util.Base64

@Serializable
data class AppSession(val sessionId: Int, val expiresAt: Long = 0) {

    fun toEncoded(): String {
        return String(Base64.getUrlEncoder().encode(Json.encodeToString(this).toByteArray()))
    }

    companion object {
        fun fromEncoded(encodedCookie: String): AppSession {
            try {
                // Decodificamos la cookie codificada en Base64
                val decodedBytes = Base64.getUrlDecoder().decode(encodedCookie)
                val decodedJson = String(decodedBytes)

                // Convertimos el JSON de nuevo a un objeto AppSession
                return Json.decodeFromString(decodedJson)
            } catch (e: Exception) {
                throw IllegalArgumentException("Error al decodificar la AppSession: ${e.message}", e)
            }
        }
    }
}