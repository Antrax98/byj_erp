package dev.byjtech.erp.core.infrastructure.api.users

import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.auth.authenticateAndAuthorize
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toDTO
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.tenantUsers() {
    get("/me") {
        val session = authenticateAndAuthorize(
            call,
            requiredAnyPermissions = listOf(
                CoreDefinition.Users.View.key,
                CoreDefinition.Users.Update.key,
                CoreDefinition.Admin.All.key
            )
        )

        call.respond(session.user.toDTO())
    }
}