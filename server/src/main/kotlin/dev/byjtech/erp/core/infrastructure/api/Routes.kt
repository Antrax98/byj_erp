package dev.byjtech.erp.core.infrastructure.api

import dev.byjtech.erp.core.auth.authenticateAndAuthorize
import dev.byjtech.erp.core.infrastructure.api.users.tenantUsers
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.ktor.server.routing.*
import dev.byjtech.erp.core.database.superAdmins.SuperAdminDataSource.Companion.isSuperAdmin
import dev.byjtech.erp.core.database.userSessions.UserSessionsDataSource.Companion.findUserIdBySessionId
import dev.byjtech.erp.core.infrastructure.api.users.superAdminUsers
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond

fun Route.CoreRoutes() {
    route("/users"){
        superAdminUsers()
        tenantUsers()
    }
    get("me/type") {
        val session = authenticateAndAuthorize(call)
        val userId = findUserIdBySessionId(session.id.value)

        if (userId == null) {
            //nunca deveria pasar
            call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
        }else{
            val userType = if (isSuperAdmin(userId)) {
                "superadmin"
            } else {
                "tenant"
            }
            call.respond(userType)
        }
    }
}