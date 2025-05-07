package dev.byjtech.erp.core.routes.tenant.users

import dev.byjtech.erp.core.CoreCorePermissions
import dev.byjtech.erp.core.auth.authenticateAndAuthorize
import dev.byjtech.erp.core.database.users.toDTO
import dev.byjtech.erp.core.dto.user.UserDTO
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.util.reflect.typeInfo

fun Route.tenantUsers() {
    get("/me") {
        val session = authenticateAndAuthorize(
            call,
            requiredAnyPermissions = listOf(
                CoreCorePermissions.Users.Read.key,
                CoreCorePermissions.Users.Update.key,
                CoreCorePermissions.Admin.All.key
            )
        )

        call.respond(session.user.toDTO())
    }
}