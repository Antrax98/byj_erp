package dev.byjtech.erp.core.api

import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.request.AssignPermissionRoleRequest
import dev.byjtech.erp.core.request.AssignRoleRequest
import dev.byjtech.erp.core.response.RolePermisisonKeysResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class RolesTenant(private val client: HttpClient) {
    suspend fun getRoleById(roleId: String): ApiResponse<RoleDTO, Unit?> {
        return try {
            val response = client.get("api/core/roles/tenant/$roleId")
            when (response.status) {
                HttpStatusCode.OK -> {
                    val role = response.body<RoleDTO>()
                    ApiResponse.Success(role)
                }
                HttpStatusCode.BadRequest -> {
                    ApiResponse.Error(null, "NO_ROLE_ID")
                }
                else -> {
                    ApiResponse.Error(null, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            ApiResponse.Error(null, "NETWORK_ERROR")
        }
    }

    suspend fun getAllRoles(): ApiResponse<Set<RoleDTO>, Unit?> {
        return try {
            val response = client.get("api/core/roles/tenant/all-roles")
            when (response.status) {
                HttpStatusCode.OK -> {
                    val roles = response.body<Set<RoleDTO>>()
                    ApiResponse.Success(roles)
                }
                HttpStatusCode.BadRequest -> {
                    ApiResponse.Error(null, "NO_COMPANY")
                }
                else -> {
                    ApiResponse.Error(null, "UNKNOWN_REQUEST_ERROR")
                }
            }

        } catch (e: Exception) {
            ApiResponse.Error(null, "NETWORK_ERROR")
        }
    }
    suspend fun assignRoleToUser(data: AssignRoleRequest): ApiResponse<Unit, Unit> {
        return try {
            val response = client.post("api/core/roles/tenant/assign-role-to-user") {
                contentType(ContentType.Application.Json)
                setBody(data)
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    ApiResponse.Error(Unit, "ERROR_ASSIGNING_ROLE")
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun getPermissionsByRoleId(roleId: String): ApiResponse<Set<PermissionWithKey>, Unit?> {
        return try {
            val response = client.get("api/core/roles/tenant/role-permissions/$roleId")
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<RolePermisisonKeysResponse>()
                    ApiResponse.Success(data.permissions)
                }
                HttpStatusCode.BadRequest -> {
                    ApiResponse.Error(null, "NO_ROLE_ID")
                }
                else -> {
                    ApiResponse.Error(null, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(null, "NETWORK_ERROR")
        }
    }

    suspend fun assignPermissionsToRole(data : AssignPermissionRoleRequest): ApiResponse<Unit, Unit> {
        return try {
            val response = client.post("api/core/roles/tenant/assign-permissions-to-role") {
                contentType(ContentType.Application.Json)
                setBody(data)
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    println(errorMessage)
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }

        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun createRole(data: RoleDTO): ApiResponse<Unit, Unit>{
        return try {
            val response = client.post("api/core/roles/tenant/create-role") {
                contentType(ContentType.Application.Json)
                setBody(data)
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }

                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }

                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun getModulesPermissionKeys(moduleIds: Set<String>): ApiResponse<Set<PermissionWithKey>, Unit?> {
        return try {
            val response = client.post("api/core/roles/tenant/modules-permissionkeys") {
                contentType(ContentType.Application.Json)
                setBody(moduleIds)
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    val permissions = response.body<Set<PermissionWithKey>>()
                    ApiResponse.Success(permissions)
                }
                else -> {
                    ApiResponse.Error(null, "UNKNOWN_REQUEST_ERROR")
                }
            }

        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(null, "NETWORK_ERROR")
        }
    }

    suspend fun deleteRolePermission(roleId: String, permissionId: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.delete("api/core/roles/tenant/delete-role-permission/$roleId/$permissionId"){
                expectSuccess = false
                contentType(ContentType.Application.Json)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun deleteUserPermission(userId: String, permissionId: String): ApiResponse<Unit, Unit> {
        return try {
            //TODO: mover ruta a users
            val response = client.delete("api/core/roles/tenant/delete-user-permission/$userId/$permissionId"){
                expectSuccess = false
                contentType(ContentType.Application.Json)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun deleteUserRole(userId: String, roleId: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.delete("api/core/roles/tenant/delete-user-role/$userId/$roleId") {
                expectSuccess = false
                contentType(ContentType.Application.Json)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun updateRoleName(roleId: String, newName: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.patch("api/core/roles/tenant/update-role-name/$roleId/$newName"){
                expectSuccess = false
                contentType(ContentType.Application.Json)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            return ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

}