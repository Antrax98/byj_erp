package dev.byjtech.erp.config

import dev.byjtech.erp.core.session.AppSession
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!DEPRECATED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

//fun Application.configureSessions() {
//    install(Sessions) {
//        cookie<AppSession>("user_session") {
//            cookie.path = "/"
//            cookie.maxAgeInSeconds = 7 * 24 * 60 * 60  // 7 días, por ejemplo
//            cookie.httpOnly = true
//            cookie.secure = false
//            cookie.extensions["SameSite"] = "Lax"
//        }
//    }
//}