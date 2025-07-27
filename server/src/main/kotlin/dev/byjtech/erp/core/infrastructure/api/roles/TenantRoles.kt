package dev.byjtech.erp.core.infrastructure.api.roles

import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.domain.model.Module
import dev.byjtech.erp.core.domain.model.Role
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

    get("/{roleId}") {
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
        val role = roleRepo.getById(UUID.fromString(roleId))
        if (role == null) {
            call.respond(HttpStatusCode.NotFound, message = "NO_ROLE")
            return@get
        }
        call.respond(HttpStatusCode.OK, role.toDTO())
    }

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
        //val permissionKeys = permissions.mapNotNull { moduleRepo.getPermissionKeyById(it.id) }
        val permissionsWithKey = moduleRepo.getPermissionsWithKeysByPermissionIds(permissions.map { it.id }.toSet())
        call.respond(HttpStatusCode.OK, RolePermisisonKeysResponse(permissions = permissionsWithKey))


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

    post("/create-role") {
        val session = authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Admin.All.key,
                CoreDefinition.Roles.Create.key
            )
        )
        val role = call.receive<dev.byjtech.erp.core.dto.RoleDTO>()
        val response: Role?
        //TODO: validar que no exista un rol con el mismo nombre en la empresa
        //todo: validar que el nombre no este vacio (porsiacaso)
        //validacion deveria ser cap sensitive?, -> por ahora si, ("admin" =/= "Admin")

        val exist = roleRepo.findByName(role.name)
        if (exist != null) {
            call.respond(HttpStatusCode.BadRequest, message = "ROLE_ALREADY_EXISTS")
            return@post
        }

        val auxRole = Role(
            name = role.name,
            description = role.description,
            companyId = session.companyId!!
        )
        try {
            response = roleRepo.create(auxRole)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, message = "ERROR_CREATING_ROLE")
            return@post
        }
        call.respond(HttpStatusCode.OK)
    }

    post("/modules-permissionkeys"){
        val session = authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Admin.All.key,
                CoreDefinition.Roles.View.key
            )
        )
        //set de UUIDs de modulos
        val request = call.receive<Set<String>>()
        val uuids = request.map { UUID.fromString(it) }.toSet()

        val response = moduleRepo.getPermissionsWithKeysByModuleIds(uuids)
        call.respond(HttpStatusCode.OK, response)
    }

    delete("/delete-role-permission/{roleId}/{permissionId}") {
        val session = authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Admin.All.key,
                CoreDefinition.Roles.Delete.key
            )
        )
        val roleId = call.parameters["roleId"]?.let(UUID::fromString)
        val permissionId = call.parameters["permissionId"]?.let(UUID::fromString)
        if(roleId==null){
            call.respond(HttpStatusCode.BadRequest, message = "NO_ROLE_ID")
            return@delete
        }
        if(permissionId==null){
            call.respond(HttpStatusCode.BadRequest, message = "NO_PERMISSION_ID")
            return@delete
        }
        roleRepo.removePermission(roleId, permissionId)
        call.respond(HttpStatusCode.OK)
    }

    delete("/delete-user-permission/{userId}/{permissionId}") {
        val session = authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Admin.All.key,
                CoreDefinition.Roles.Delete.key
            )
        )
        val userId = call.parameters["userId"]?.let(UUID::fromString)
        val permissionId = call.parameters["permissionId"]?.let(UUID::fromString)
        if(userId==null){
            call.respond(HttpStatusCode.BadRequest, message = "NO_USER_ID")
            return@delete
        }
        if(permissionId==null){
            call.respond(HttpStatusCode.BadRequest, message = "NO_PERMISSION_ID")
            return@delete
        }
        val response = userRepo.removeSpecialPermission(userId, permissionId)
        if(response) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.BadRequest, message = "ERROR_REMOVING_PERMISSION")
        }
    }

    delete("/delete-user-role/{userId}/{roleId}"){
        val session = authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Admin.All.key,
                CoreDefinition.Roles.Delete.key
            )
        )
        val userId = call.parameters["userId"]?.let(UUID::fromString)
        val roleId = call.parameters["roleId"]?.let(UUID::fromString)

        if(userId==null){
            call.respond(HttpStatusCode.BadRequest, message = "NO_USER_ID")
            return@delete
        }
        if(roleId==null){
            call.respond(HttpStatusCode.BadRequest, message = "NO_ROLE_ID")
            return@delete
        }
        val response = userRepo.removeRole(userId, roleId)
        if(response) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.BadRequest, message = "ERROR_REMOVING_ROLE")
        }
    }

    patch("/update-role-name/{roleId}/{name}") {
        val session = authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Admin.All.key,
                CoreDefinition.Roles.Update.key
            )
        )
        val roleId = call.parameters["roleId"]
        if (roleId == null) {
            call.respond(HttpStatusCode.BadRequest, message = "NO_ROLE_ID")
            return@patch
        }
        val newName = call.parameters["name"]
        if (newName == null) {
            call.respond(HttpStatusCode.BadRequest, message = "NO_NEW_NAME")
            return@patch
        }
        val role = roleRepo.getById(UUID.fromString(roleId))
        if (role == null) {
            call.respond(HttpStatusCode.NotFound, message = "NO_ROLE")
            return@patch
        }
        roleRepo.updateName(role.id, newName)
        call.respond(HttpStatusCode.OK)


    }

}