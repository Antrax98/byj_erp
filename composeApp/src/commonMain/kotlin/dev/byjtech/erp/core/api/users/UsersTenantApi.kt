package dev.byjtech.erp.core.api.users

import de.jensklingenberg.ktorfit.http.*
import dev.byjtech.erp.common.api.ApiResponse
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.request.AssignRoleRequest
import dev.byjtech.erp.core.request.AssignSpecialPermissionRequest
import dev.byjtech.erp.core.response.CompanyUsersResponse


interface UsersTenantApi {

    //TODO(): usar ApiResponse en los que no lo usen

    @GET("api/core/users/tenant/me")
    suspend fun getaActualUser(): UserDTO

    @GET("api/core/users/tenant/company-users")
    suspend fun getCompanyUsers(): CompanyUsersResponse?

    @GET("api/core/users/tenant/{userId}")
    suspend fun getUser(@Path("userId") userId: Int): UserDTO?

    @POST("api/core/users/assign-role")
    suspend fun assignRole(@Body assignRoleRequest: AssignRoleRequest): ApiResponse<Unit>

    @POST("api/core/users/assign-special-permission")
    suspend fun assignSpecialPermission(@Body assignSpecialPermissionRequest: AssignSpecialPermissionRequest): ApiResponse<Unit>

    @DELETE("api/core/users/unassign-role/{userId}/{roleId}")
    suspend fun unassignRole(@Path("userId") userId: Int, @Path("roleId") roleId: Int): ApiResponse<Unit>

    @DELETE("api/core/users/unassign-special-permission/{userId}/{permissionId}")
    suspend fun unassignSpecialPermission(@Path("userId") userId: Int, @Path("permissionId") permissionId: Int): ApiResponse<Unit>

    @POST("api/core/users/create-user")
    suspend fun createUser(@Body userDTO: UserDTO): ApiResponse<Unit>

}