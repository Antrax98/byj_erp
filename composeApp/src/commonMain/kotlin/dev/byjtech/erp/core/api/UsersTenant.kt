package dev.byjtech.erp.core.api

import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.request.AssignRoleRequest
import dev.byjtech.erp.core.request.AssignSpecialPermissionRequest
import dev.byjtech.erp.core.response.CompanyUsersResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class UsersTenant(private val client: HttpClient) {
    suspend fun getActualUser(): UserDTO {
        return try {
            val response = client.get("api/core/users/tenant/me") {
                expectSuccess = false
            }
            response.body<UserDTO>()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getCompanyUsers(): CompanyUsersResponse? {
        return try {
            val response = client.get("api/core/users/tenant/company-users") {
                expectSuccess = false
            }
            response.body<CompanyUsersResponse>()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getUser(userId: String): UserDTO? {
        return try {
            val response = client.get("api/core/users/tenant/$userId") {
                expectSuccess = false
            }
            response.body<UserDTO>()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun assignRole(assignRoleRequest: AssignRoleRequest): ApiResponse<Unit,Unit> {
        return try {
            val response = client.post("api/core/users/assign-role") {
                contentType(ContentType.Application.Json)
                setBody(assignRoleRequest)
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
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun assignSpecialPermission(
        assignSpecialPermissionRequest: AssignSpecialPermissionRequest
    ): ApiResponse<Unit, Unit> {
        return try {
            val response = client.post("api/core/users/assign-special-permission") {
                contentType(ContentType.Application.Json)
                setBody(assignSpecialPermissionRequest)
            }
            when (response.status) {
                HttpStatusCode.OK -> ApiResponse.Success(Unit)
                HttpStatusCode.BadRequest -> ApiResponse.Error(Unit, response.bodyAsText())
                else -> ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun unassignRole(userId: String, roleId: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.delete("api/core/users/unassign-role/$userId/$roleId")
            when (response.status) {
                HttpStatusCode.OK -> ApiResponse.Success(Unit)
                HttpStatusCode.BadRequest -> ApiResponse.Error(Unit, response.bodyAsText())
                else -> ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun unassignSpecialPermission(userId: String, permissionId: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.delete("api/core/users/unassign-special-permission/$userId/$permissionId")
            when (response.status) {
                HttpStatusCode.OK -> ApiResponse.Success(Unit)
                HttpStatusCode.BadRequest -> ApiResponse.Error(Unit, response.bodyAsText())
                else -> ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun createUser(userDTO: UserDTO): ApiResponse<Unit, Unit> {
        return try {
            val response = client.post("api/core/users/create-user") {
                contentType(ContentType.Application.Json)
                setBody(userDTO)
            }
            when (response.status) {
                HttpStatusCode.OK -> ApiResponse.Success(Unit)
                HttpStatusCode.BadRequest -> ApiResponse.Error(Unit, response.bodyAsText())
                else -> ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

}