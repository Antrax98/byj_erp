package dev.byjtech.erp.core.infrastructure.api.roles

import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.domain.repository.RoleRepository
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toDTO
import dev.byjtech.erp.core.request.AssignPermissionRoleRequest
import dev.byjtech.erp.core.request.AssignRoleRequest
import dev.byjtech.erp.core.response.RolePermisisonKeysResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.*
import java.util.UUID

fun Route.tenantRoles(authServ: CoreAuthWrapper, roleRepo: RoleRepository, userRepo: UserRepository, moduleRepo: ModuleRepository) {

    get("/all-roles") {
        val session = authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Admin.All.key,
                CoreDefinition.Roles.View.key
            )
        )

        if (session.companyId == null) {
            call.respond(HttpStatusCode.BadRequest, message = "NO_COMPANY")
            return@get
        }

        val roles = roleRepo.findByCompanyId(session.companyId)
        val rolesDTO = roles.map { it.toDTO() }.toSet()

        call.respond(HttpStatusCode.OK, rolesDTO)
    }

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
        val response = userRepo.addRole(UUID.fromString(assignRoleData.userId), UUID.fromString(assignRoleData.roleId))

        if (response) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.BadRequest, message = "ERROR_ASSIGNING_ROLE")
        }
    }

    get("/role-permissions/{roleId}") {
        val session = authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Admin.All.key,
                CoreDefinition.Roles.View.key
            )
        )

        val roleId = call.parameters["roleId"]
        if (roleId == null) {
            call.respond(HttpStatusCode.BadRequest, message = "NO_ROLE_ID")
            return@get
        }
        val permissions = roleRepo.getPermissionsByRoleId(UUID.fromString(roleId))
        val permissionKeys = permissions.mapNotNull { moduleRepo.getPermissionKeyById(it.id) }

        call.respond(HttpStatusCode.OK, RolePermisisonKeysResponse(permissions = permissionKeys.toSet()))


    }

    post("/assign-permissions-to-role") {
        val session = authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Admin.All.key,
                CoreDefinition.Roles.Assign.key
            )
        )

        val request = call.receive<AssignPermissionRoleRequest>()

        if (request.permissionKeys == null && request.permissionIds == null) {
            call.respond(HttpStatusCode.BadRequest, message = "NO_PERMISSIONS")
            return@post
        }

        val role = roleRepo.getById(UUID.fromString(request.roleId))

        if (role == null) {
            call.respond(HttpStatusCode.BadRequest, message = "NO_ROLE")
            return@post
        }

        if (request.permissionKeys != null) {
            val permissionsSet = moduleRepo.findPermissionsByPermissionKeySet(request.permissionKeys!!)
            roleRepo.addPermissions(role.id, permissionsSet.map { it.id }.toSet())

        } else {
            roleRepo.addPermissions(role.id, request.permissionIds!!.map { UUID.fromString(it) }.toSet())
        }

        call.respond(HttpStatusCode.OK)
    }

}