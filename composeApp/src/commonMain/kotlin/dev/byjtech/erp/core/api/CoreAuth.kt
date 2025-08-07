package dev.byjtech.erp.core.api

import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.response.AccessibleModulesResponse
import dev.byjtech.erp.core.response.PermissionKeysResponse
import dev.byjtech.erp.core.response.PermittedModulesResponse
import dev.byjtech.erp.core.response.SubscribedModulesResponse
import dev.byjtech.erp.core.response.UserPermissionsResponse
import dev.byjtech.erp.core.response.UserRolesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.get

class CoreAuth(private val client: HttpClient) {
    suspend fun test(): String {
        return try {
            val response = client.get("auth/test")
            response.body()
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun logout(): String {
        return try {
            val response = client.get("auth/logout"){
                expectSuccess = false
            }
            response.body()
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun login(): String {
        return try {
            val response = client.get("auth/login"){
                expectSuccess = false
            }
            response.body()
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun getMyType(): String {
        return try {
            val response = client.get("auth/me/type"){
                expectSuccess = false
            }
            response.body()
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun getMe(): UserDTO {
        return try {
            val response = client.get("auth/me") {
                expectSuccess = false
            }
            response.body<UserDTO>()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun permittedModules(): PermittedModulesResponse {
        return try {
            val response = client.get("auth/permitted-modules") {
                expectSuccess = false
            }
            response.body<PermittedModulesResponse>()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun subscribedModules(): SubscribedModulesResponse {
        return try {
            val response = client.get("auth/subscribed-modules") {
                expectSuccess = false
            }
            response.body<SubscribedModulesResponse>()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun accessibleModules(): AccessibleModulesResponse {
        return try {
            val response = client.get("auth/accessible-modules") {
                expectSuccess = false
            }
            response.body<AccessibleModulesResponse>()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun userPermissions(): UserPermissionsResponse {
        return try {
            val response = client.get("auth/user-permissions") {
                expectSuccess = false
            }
            response.body<UserPermissionsResponse>()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun userRoles(userId: String): UserRolesResponse {
        return try {
            val response = client.get("auth/userRoles/$userId") {
                expectSuccess = false
            }
            response.body<UserRolesResponse>()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getSpecialPermissionsByUserId(userId: String): PermissionKeysResponse {
        return try {
            val response = client.get("auth/userSpecialPermissions/$userId") {
                expectSuccess = false
            }
            response.body<PermissionKeysResponse>()
        } catch (e: Exception) {
            throw e
        }
    }

}