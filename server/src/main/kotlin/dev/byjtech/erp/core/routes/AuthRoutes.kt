package dev.byjtech.erp.core.routes

import com.github.benmanes.caffeine.cache.Cache
import dev.byjtech.erp.config.fetchGoogleUserInfo
import dev.byjtech.erp.core.auth.authenticateAndAuthorize
import dev.byjtech.erp.core.database.userSessions.UserSessionsDataSource
import dev.byjtech.erp.core.database.userSessions.UserSessionsDataSource as USDS
import dev.byjtech.erp.core.database.users.findUserByEmail
import dev.byjtech.erp.core.database.users.updateUserFromGoogleInfo
import dev.byjtech.erp.core.session.AppSession
import java.time.LocalDateTime
import io.ktor.client.HttpClient
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import io.ktor.util.toMap
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.Base64

val logger: Logger = LoggerFactory.getLogger("AuthCallbackLogger")

//data class UserPrincipal(
//    val userId: Int,
//    val name: String,
//    val email: String,
//    val picture: String?
//)



fun Route.googleAuthRoutes(httpClient: HttpClient, stateCache: Cache<String, String>) {
    get("/test") {
        val session = authenticateAndAuthorize(call)
        println("Request Info:")
        println("Method: ${call.request.httpMethod}")
        println("URL: ${call.request.uri}")
        println("Headers: ${call.request.headers.toMap()}")
        println("Body: ${call.receiveText()}")
        call.respondText("Debugging complete.")
    }

    route("/logout"){
        get {
            val session = authenticateAndAuthorize(call)
            println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
            UserSessionsDataSource.deleteSessionById(session.id.value)
            call.respondText("Logout", status = HttpStatusCode.OK)
        }

    }


    authenticate("google-oauth") {
        get("/login") {
            val stateEncoded = call.request.queryParameters["state"]
            logger.debug("stateEncoded: {}", stateEncoded)
            val stateJson = String(Base64.getUrlDecoder().decode(stateEncoded))
            logger.debug("stateJson {}",stateJson)
            val stateMap = Json.decodeFromString<Map<String, String>>(stateJson)
            logger.debug("stateMap {}",stateMap)
            val platform = stateMap["platform"] ?: "unknown"
            val deviceId = stateMap["deviceId"] ?: "unknown"
            logger.debug("decoded platform: {}, deviceId: {}",platform,deviceId)
        }
        get("/callback") {
            logger.debug("Entering /callback")
            val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
            logger.info("principal = $principal")
            val stateEncoded = stateCache.getIfPresent(call.request.queryParameters["state"]?:"")
            stateCache.invalidate(call.request.queryParameters["state"]?:"")
            logger.debug("stateEncoded: {}", stateEncoded)
            val redirect = call.request.queryParameters["redirect"]
            logger.debug("Entering /callback. state encoded: {}, redirect: {}", stateEncoded, redirect)
            if (principal != null && stateEncoded != null) {

                // se decodifica el state enviado por el frontend
                val stateJson = String(Base64.getUrlDecoder().decode(stateEncoded))
                logger.debug("stateJson {}",stateJson)
                val stateMap = Json.decodeFromString<Map<String, String>>(stateJson)
                logger.debug("stateMap {}",stateMap)
                val platform = stateMap["platform"] ?: "unknown"
                val deviceId = stateMap["deviceId"] ?: "unknown"
                logger.debug("decoded platform: {}, deviceId: {}",platform,deviceId)
                val accessToken = principal.accessToken
                val idToken = principal.extraParameters["id_token"]
                val googleUserInfo = fetchGoogleUserInfo(httpClient, accessToken, idToken)


                if (googleUserInfo != null) {
                    val userEntity = findUserByEmail(googleUserInfo.email)
                    if (userEntity != null) {
                        updateUserFromGoogleInfo(userEntity, googleUserInfo)
                        val userAgent = call.request.headers["User-Agent"] ?: ""
                        val sessionExpiresAt = if (googleUserInfo.expiresAt > 0) {
                            Instant.ofEpochMilli(googleUserInfo.expiresAt)
                        } else {
                            logger.warn("No expiration found in ID token. Using default (7 days).")
                            LocalDateTime.now().plusDays(7).toInstant(java.time.ZoneOffset.UTC) //Example: 7 days from now. Adjust as needed
                        }
                        val existingSession = USDS.findSessionByUserIdAndDeviceId(userEntity.id.value, deviceId)
                        val sessionCookie : AppSession
                        if (existingSession != null && existingSession.isValid) {
                            // actualizar el ya existente
                            USDS.updateSession(
                                existingSession.id.value,
                                accessToken = accessToken,
                                refreshToken = principal.refreshToken ?: "",
                                userAgent = userAgent,
                                platform = platform,
                                expiresAt = sessionExpiresAt // Update expiration
                            )
                            sessionCookie = AppSession(existingSession.id.value, sessionExpiresAt.toEpochMilli())
                            logger.debug("Updated appSession: {}", sessionCookie)
                        } else {
                            val newSession = USDS.createSession(
                                userId = userEntity.id.value,
                                accessToken = accessToken,
                                refreshToken = principal.refreshToken ?: "",
                                userAgent = userAgent,
                                deviceId = deviceId,
                                platform = platform,
                                expiresAt = sessionExpiresAt  // Store expiration
                            )
                            sessionCookie = AppSession(newSession.id.value, sessionExpiresAt.toEpochMilli())
                            logger.debug("Created new appSession: {}", sessionCookie)
                        }
                        val jsonCookie = Json.encodeToString(AppSession.serializer(), sessionCookie)
                        val encodedCookie = Base64.getUrlEncoder().encodeToString(jsonCookie.toByteArray())
                        logger.debug("Redirecting from /callback to: {}, platform {}", "$redirect?appSession=$encodedCookie",platform)
                        // dependiendo de la plataforma, hay que usar un redirect diferente, aqui añadir el de IOS de implementarse
                        when (platform) {
                            "android" -> {
                                // Redirigir a erpapp://callback
                                call.respondRedirect("erpapp://callback?appSession=$encodedCookie")
                                //call.respondRedirect("$redirect?appSession=$encodedCookie")
                            }
                            "desktop" -> {
                                // Redirigir a localhost
                                call.respondRedirect("http://localhost:12345/callback?appSession=$encodedCookie")
                            }
                            "web" -> {
                                //settear la cookie en el header

                            }
                            else -> {
                                // Redirección por defecto o manejo de error
                                call.respondText("unknown platform",status = HttpStatusCode.BadRequest, contentType = ContentType.Text.Plain)
                            }
                        }
                    } else {
                        call.respondText("User not found. Access Denied.", status = HttpStatusCode.Unauthorized, contentType = ContentType.Text.Plain)
                    }
                } else {
                    call.respondText("Failed to fetch user information from Google.", status = HttpStatusCode.InternalServerError, contentType = ContentType.Text.Plain)
                }
            } else {
                logger.error("principal or stateEncoded is null")
                call.respondText("Authentication failed.", status = HttpStatusCode.Unauthorized, contentType = ContentType.Text.Plain)
            }
        }
    }

}