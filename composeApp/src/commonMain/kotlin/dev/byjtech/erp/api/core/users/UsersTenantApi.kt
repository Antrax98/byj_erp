package dev.byjtech.erp.api.core.users

import de.jensklingenberg.ktorfit.http.*
import dev.byjtech.erp.core.dto.user.UserDTO


interface UsersTenantApi {

    @GET("api/core/core/users/tenant/me")
    suspend fun getUserById(): UserDTO


}