package dev.byjtech.erp.core.infrastructure.api.users

import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.shared.ApiResponse
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toDTO
import dev.byjtech.erp.core.request.AssignRoleRequest
import dev.byjtech.erp.core.request.AssignSpecialPermissionRequest
import dev.byjtech.erp.core.response.CompanyUsersResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import java.util.UUID

fun Route.tenantUsers(authServ: CoreAuthWrapper, userRepo: UserRepository, moduleRepo: ModuleRepository) {
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
        authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Users.View.key,
                CoreDefinition.Admin.All.key
            )
        )
        val userId = call.parameters["userId"]?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
        val user = userRepo.find(UUID.fromString(userId))
        if (user == null) {
            call.respond(HttpStatusCode.NotFound, null)
            return@get
        } else {
            call.respond(user.toDTO())
        }
    }

    post("/assign-role") {
        authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Admin.All.key,
                CoreDefinition.Roles.Assign.key
            )
        )
        val assignRoleData = call.receive<AssignRoleRequest>()

        //TODO(): hacer que reconosca cuando no existe algun dato o simplemente ya existia la asignacion (SealedClass?)
        val response = userRepo.addRole(UUID.fromString(assignRoleData.userId), UUID.fromString(assignRoleData.roleId))

        if (response) {
            call.respond(HttpStatusCode.OK, ApiResponse.Success(Unit))

        } else {
            call.respond(HttpStatusCode.BadRequest, ApiResponse.Error("Failed to assign role", "FAILED_TO_ASSIGN_ROLE"))
        }
    }

    post("/assign-special-permission") {
        authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Roles.Assign.key,
                CoreDefinition.Admin.All.key
            )
        )
        val assignSpecialPermissionData = call.receive<AssignSpecialPermissionRequest>()


        if(assignSpecialPermissionData.permissionId != null || assignSpecialPermissionData.permissionKey != null){
            var permissionId: UUID? = null
            if (assignSpecialPermissionData.permissionId != null){
                //logica con permissionId
                permissionId = UUID.fromString(assignSpecialPermissionData.permissionId)

            } else {
                //logica con permissionKey
                val permission = moduleRepo.findPermissionByPermissionKey(
                    assignSpecialPermissionData.permissionKey!!
                )
                if (permission != null){
                    permissionId = permission.id
                } else {
                    call.respond(HttpStatusCode.BadRequest, "PERMISSION_NOT_FOUND")
                    return@post
                }
            }
            //logica comun
            if (permissionId != null){
                val response = userRepo.addSpecialPermission(UUID.fromString(assignSpecialPermissionData.userId), permissionId)
                if (response) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "PERMISSION_NOT_ASSIGNED")
                }
            }

        } else {
            call.respond(HttpStatusCode.BadRequest, "NO_PERMISSION_PROVIDED")
        }


    }

    //TODO(): borrar
    delete("unassign-special-permission/{userId}/{permissionId}"){
        authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Roles.Assign.key,
                CoreDefinition.Admin.All.key
            )
        )
        val userId = call.parameters["userId"]?: return@delete call.respond(HttpStatusCode.BadRequest, ApiResponse.Error( "Invalid user ID", code = "INVALID_USER_ID"))
        val permissionId = call.parameters["permissionId"]?: return@delete call.respond(HttpStatusCode.BadRequest, ApiResponse.Error("Invalid permission ID", code = "INVALID_PERMISSION_ID"))

        val response = userRepo.removeSpecialPermission(UUID.fromString(userId), UUID.fromString(permissionId))
        if (response) {
            call.respond(
                HttpStatusCode.OK,
                ApiResponse.Success(Unit)
            )
        } else {
            call.respond(
                HttpStatusCode.BadRequest,
                ApiResponse.Error( "Special permission not removed", code = "SPECIAL_PERMISSION_NOT_REMOVED")
            )
        }
    }

    //TODO(): borrar
    delete("/unassign-role/{userId}/{roleId}"){
        authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Admin.All.key,
                CoreDefinition.Roles.Assign.key
            )
        )
        val userId = call.parameters["userId"]?: return@delete call.respond(HttpStatusCode.BadRequest, ApiResponse.Error("Invalid user ID", code = "INVALID_USER_ID"))
        val roleId = call.parameters["roleId"]?: return@delete call.respond(HttpStatusCode.BadRequest, ApiResponse.Error("Invalid role ID", code = "INVALID_ROLE_ID"))
        val response = userRepo.removeRole(UUID.fromString(userId), UUID.fromString(roleId))
        if (response) {
            call.respond(HttpStatusCode.OK, ApiResponse.Success(Unit))
        } else {
            call.respond(HttpStatusCode.BadRequest, ApiResponse.Error("Failed to remove role", code = "FAILED_TO_REMOVE_ROLE"))
        }
    }

    //el id de la compañia debe estar en el DTO
    post("/create-user"){
        val session = authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Users.Create.key,
                CoreDefinition.Admin.All.key
            )
        )
        var newUser = call.receive<UserDTO>()
        if (newUser.companyId == null) {
            val userCompanyid = userRepo.find(session.userId)?.companyId
            if (userCompanyid == null) {
                call.respond(HttpStatusCode.BadRequest, "COMPANY_ID_NOT_FOUND")
                return@post
            } else {
                newUser = newUser.copy(companyId = userCompanyid.toString())
            }
        }
        //TODO: validar que el email no exista en la base de datos
        if (newUser.email.isNotEmpty()) {
            val exist = userRepo.findByEmail(newUser.email)
            if (exist != null) {
                call.respond(HttpStatusCode.BadRequest, "USER_EMAIL_ALREADY_EXISTS")
                return@post
            }
        }

        val response = userRepo.create(newUser)
        if (response) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.BadRequest, "FAILED_TO_CREATE_USER")
        }
    }

    get("/user-special-permissions/{userId}"){
        authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Users.View.key,
                CoreDefinition.Admin.All.key
            )
        )
        val userId = call.parameters["userId"]?: return@get call.respond(HttpStatusCode.BadRequest, "INVALID_USER_ID")
        val permissions = userRepo.getSpecialPermissionsByUserId(UUID.fromString(userId))
        if (permissions == null) {
            call.respond(HttpStatusCode.OK, emptySet<PermissionWithKey>())
//            call.respond(HttpStatusCode.NotFound, "NO_SPECIAL_PERMISSIONS")
            return@get
        }
        val permissionsWithKey = moduleRepo.getPermissionsWithKeysByPermissionIds(permissions.map { it.id }.toSet())
        call.respond(HttpStatusCode.OK, permissionsWithKey)
    }

    patch("/update-user-name/{userId}/{name}") {
        authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Users.Update.key,
                CoreDefinition.Admin.All.key
            )
        )
        val userId = call.parameters["userId"]
        if (userId == null) {
            call.respond(HttpStatusCode.BadRequest, "NO_USER_ID")
            return@patch
        }
        val newName = call.parameters["name"]
        if (newName == null) {
            call.respond(HttpStatusCode.BadRequest, "NO_NEW_NAME")
            return@patch
        }
        val user = userRepo.find(UUID.fromString(userId))
        if (user == null) {
            call.respond(HttpStatusCode.NotFound, "NO_USER")
            return@patch
        }
        userRepo.updateName(user.id, newName)
        call.respond(HttpStatusCode.OK)

    }

}