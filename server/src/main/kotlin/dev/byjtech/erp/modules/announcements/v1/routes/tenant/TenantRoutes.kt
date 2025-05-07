package dev.byjtech.erp.modules.announcements.v1.routes.tenant

import dev.byjtech.erp.core.Core
import dev.byjtech.erp.core.auth.authenticateAndAuthorize
import dev.byjtech.erp.modules.announcements.v1.AnnouncementsV1
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.tenantRoutes() {
    get("/me") {
        val session = authenticateAndAuthorize(
            call,
            requiredAnyPermissions = listOf(
                AnnouncementsV1.Misc.Read.key,
                Core.Users.Read.key
            )
        )
        call.respondText("Hello Tenant ${session?.user?.name} with permission: ${AnnouncementsV1.Misc.Read.key.toString()}")
    }
}