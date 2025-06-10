package dev.byjtech.erp.core.infrastructure.api.roles

import dev.byjtech.erp.shared.ApiResponse
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.domain.repository.RoleRepository
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.core.request.AssignRoleRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.tenantRoles(authServ: CoreAuthWrapper, roleRepo: RoleRepository, userRepo: UserRepository) {
    post("/assign-role-to-user") {
        val session = authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Admin.All.key,
                CoreDefinition.Roles.Assign.key
            )
        )
        val assignRoleData = call.receive<AssignRoleRequest>()

        //TODO(): hacer que reconosca cuando no existe algun dato o simplemente ya existia la asignacion
        val response = userRepo.addRole(assignRoleData.userId, assignRoleData.roleId)

        if (response) {
            call.respond(HttpStatusCode.OK, ApiResponse.Success(Unit))
        } else {
            call.respond(HttpStatusCode.BadRequest, ApiResponse.Error("Failed to assign role", code = "ROLE_ASSIGN_FAILED"))
        }


    }

}