package dev.byjtech.erp.config

import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.auth.OAuthServerSettings
import kotlinx.serialization.Serializable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.auth0.jwt.JWT
import com.github.benmanes.caffeine.cache.Cache
import java.util.Date
import java.util.UUID

val logger: Logger = LoggerFactory.getLogger("AuthConfig")

val dotenv = dotenv {
    ignoreIfMissing = false
}

fun Application.configureOAuth(httpClient: HttpClient, stateCache: Cache<String, String>) {

    install(Authentication) {
        oauth("google-oauth") {
            urlProvider = { dotenv["OAUTH_REDIRECT_URI"] }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    //accessTokenUrl = "https://oauth2.googleapis.com/token", //usable con https
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = dotenv["GOOGLE_CLIENT_ID"],
                    clientSecret = dotenv["GOOGLE_CLIENT_SECRET"],
                    defaultScopes = listOf("openid","https://www.googleapis.com/auth/userinfo.profile","https://www.googleapis.com/auth/userinfo.email"),
                    extraAuthParameters = listOf(
                        "access_type" to "offline",
                        "prompt" to "consent"
                        ),
                    onStateCreated = { call, state -> stateCache.put(state, call.request.queryParameters["state"] ?: "") }
                )
            }
            client = httpClient
        }
    }
}


suspend fun fetchGoogleUserInfo(httpClient: HttpClient, accessToken: String, idToken: String? = null): UserInfo? { // Modified to accept idToken
    val userInfoResponse = httpClient.get("https://www.googleapis.com/oauth2/v3/userinfo") {
        headers {
            append(HttpHeaders.Authorization, "Bearer $accessToken")
        }
    }
    logger.debug("Response from Google User Info API: ${userInfoResponse.status}")

    return if (userInfoResponse.status == HttpStatusCode.OK) {
        val googleUser = userInfoResponse.body<GoogleUserResponse>()
        val expiresAt = idToken?.let { getExpirationFromIdToken(it) } // Extract expiration from idToken
        UserInfo(
            sub = googleUser.sub,
            name = googleUser.name,
            email = googleUser.email,
            picture = googleUser.picture,
            expiresAt = expiresAt?.time ?: 0  // Include expiration in UserInfo, convert to Long (ms)
        )
    } else {
        val errorBody = userInfoResponse.bodyAsText()
        logger.error("Error from Google User Info API: ${userInfoResponse.status}, Body: $errorBody")
        null
    }
}
fun getExpirationFromIdToken(idToken: String): Date? {
    return try {
        val decodedJWT = JWT.decode(idToken)
        decodedJWT.expiresAt
    } catch (e: Exception) {
        logger.error("Error decoding ID token: ${e.message}")
        null
    }
}

@Serializable
data class UserInfo(
    val sub: String,
    val name:String,
    val email:String,
    val picture: String?,
    val expiresAt: Long
)

@Serializable
data class GoogleUserResponse(
    val sub: String,
    val name:String,
    val email:String,
    val picture: String?
)