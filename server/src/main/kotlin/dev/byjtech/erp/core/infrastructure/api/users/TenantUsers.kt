package dev.byjtech.erp.core.infrastructure.api.users

import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.auth.authenticateAndAuthorize
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toDTO
import dev.byjtech.erp.core.response.CompanyUsersResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.tenantUsers(authServ: CoreAuthWrapper, userRepo: UserRepository) {
    get("/me") {

        val session = authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Users.View.key,
                CoreDefinition.Users.Update.key,
                CoreDefinition.Admin.All.key
            )
        )

        val user = userRepo.find(session.userId)
        if (user == null) {
            call.respond(HttpStatusCode.NotFound, "User not found")
            return@get
        } else {
            call.respond(user.toDTO())

        }
    }

    get("/company-users"){
        val session = authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Users.View.key,
                CoreDefinition.Admin.All.key
            )
        )
        val actUser = userRepo.find(session.userId)
        if (actUser?.companyId == null) {
            call.respond(HttpStatusCode.NotFound, "User not found")
            return@get
        }

        val users = userRepo.findByCompanyId(actUser.companyId)
        call.respond(CompanyUsersResponse(users.map { it.toDTO() })) // Set<User> -> List<UserDTO>
    }

    get("/{userId}"){
        val session = authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Users.View.key,
                CoreDefinition.Admin.All.key
            )
        )
        val userId = call.parameters["userId"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
        val user = userRepo.find(userId)
        if (user == null) {
            call.respond(HttpStatusCode.NotFound, null)
            return@get
        } else {
            call.respond(user.toDTO())
        }
    }
}