package dev.byjtech.erp.modules.announcements.v1.routes.superAdmin

import dev.byjtech.erp.core.auth.authenticateAndAuthorize
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.superAdminRoutes() {
        get("/me") {
            val session = authenticateAndAuthorize(call,requiredAdmin = true)
            call.respondText("Hello SuperAdmin ${session?.user?.name}")
        }
}