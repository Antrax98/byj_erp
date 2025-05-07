package dev.byjtech.erp.config

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!DEPRECATED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


//
//import dev.byjtech.erp.core.database.userSessions.UserSessionsDataSource
//import dev.byjtech.erp.core.session.AppSession
//import io.ktor.server.application.*
//import io.ktor.server.auth.*
//import io.ktor.server.auth.oauth.*
//import io.ktor.server.response.*
//import io.ktor.client.*
//import io.ktor.client.engine.cio.*
//import io.ktor.client.request.*
//import io.ktor.http.*
//import io.ktor.serialization.kotlinx.json.*
//import io.ktor.server.sessions.*
//import kotlinx.serialization.json.Json
//import io.ktor.client.plugins.contentnegotiation.*
//import io.ktor.server.routing.*
//import io.github.cdimascio.dotenv.dotenv
//
//fun Application.configureSecurity(userSessionsDataSource: UserSessionsDataSource) {
//    val dotenv = dotenv()
//    val googleClientId = dotenv["GOOGLE_CLIENT_ID"]
//    val googleClientSecret = dotenv["GOOGLE_CLIENT_SECRET"]
//    val redirectUrl = dotenv["OAUTH_REDIRECT_URI"]
//
//    install(Sessions) {
//        cookie<AppSession>("user_session") {
//            cookie.path = "/"
//            cookie.maxAgeInSeconds = 7 * 24 * 60 * 60
//            cookie.httpOnly = true
//            // cookie.secure = true // Enable in production with HTTPS
//            cookie.extensions["SameSite"] = "Lax"
//        }
//    }
//
//    val clientt = HttpClient(CIO) {
//        install(ContentNegotiation) {
//            json(Json {
//                ignoreUnknownKeys = true
//            })
//        }
//    }
//
//    install(Authentication) {
//        oauth("google-oauth") {
//            urlProvider = { redirectUrl }
//            providerLookup = {
//                OAuth2Provider(
//                    name = "google",
//                    clientId = googleClientId,
//                    clientSecret = googleClientSecret,
//                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
//                    tokenUrl = "https://accounts.google.com/o/oauth2/token",
//                    defaultScopes = listOf("profile", "https://www.googleapis.com/auth/userinfo.profile")
//                )
//            }
//            client = clientt
//        }
//
//        session<AppSession>("session-auth") {
//            validate { session ->
//                val sessionData = userSessionsDataSource.findSessionByEntityId(session.userSessionEntityId)
//                if (sessionData != null && sessionData.isValid) {
//                    // You might want to create a custom Principal object here if needed
//                    UserIdPrincipal(sessionData.user.id.value.toString())
//                } else {
//                    null
//                }
//            }
//            challenge {
//                call.sessions.clear<AppSession>()
//                call.respondRedirect("/")
//            }
//        }
//    }
//}
//
//// Example route (you might have this elsewhere)
//fun Route.protectedRoute(userSessionsDataSource: UserSessionsDataSource) {
//    authenticate("session-auth") {
//        get("/protected") {
//            val appSession = call.sessions.get<AppSession>()
//            val sessionEntityId = appSession?.userSessionEntityId
//            val sessionData = sessionEntityId?.let { userSessionsDataSource.findSessionByEntityId(it) }
//
//            if (sessionData != null && sessionData.isValid) {
//                val user = sessionData.user
//                call.respondText("Hello, ${user.name}! (Protected area)")
//            } else {
//                call.respondRedirect("/")
//            }
//        }
//    }
//}