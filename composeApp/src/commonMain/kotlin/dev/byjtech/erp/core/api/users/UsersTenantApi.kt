package dev.byjtech.erp.core.api.users

import de.jensklingenberg.ktorfit.http.*
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.response.CompanyUsersResponse


interface UsersTenantApi {

    @GET("api/core/users/tenant/me")
    suspend fun getaActualUser(): UserDTO

    @GET("api/core/users/tenant/company-users")
    suspend fun getCompanyUsers(): CompanyUsersResponse?

    @GET("api/core/users/tenant/{userId}")
    suspend fun getUser(@Path("userId") userId: Int): UserDTO?

}