package dev.byjtech.erp.core.api

import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.request.AssignSpecialPermissionRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class UsersT(private val client: HttpClient){
    suspend fun createUser(userDTO: UserDTO):ApiResponse<Unit, Unit> {
        return try {
            val response = client.post("api/core/users/tenant/create-user") {
                contentType(ContentType.Application.Json)
                setBody(userDTO)
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                    //
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Error){
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun getUserSpecialPermissionsWithKey(userId: String): ApiResponse<Set<PermissionWithKey>, Unit> {
        return try {
            val response = client.get("api/core/users/tenant/user-special-permissions/$userId")
            when (response.status) {
                HttpStatusCode.OK -> {
                    val permissions = response.body<Set<PermissionWithKey>>()
                    ApiResponse.Success(permissions)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception){
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun assignSpecialPermission(userId: String, permissionKey: PermissionKey): ApiResponse<Unit, Unit> {
        val data = AssignSpecialPermissionRequest(userId = userId, permissionId = null, permissionKey =  permissionKey)
        return try {
            val response = client.post("api/core/users/tenant/assign-special-permission") {
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
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun updateUserName(userId: String, newName: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.patch("api/core/users/tenant/update-user-name/$userId/$newName"){
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
}