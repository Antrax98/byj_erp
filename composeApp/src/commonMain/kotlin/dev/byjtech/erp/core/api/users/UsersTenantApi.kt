package dev.byjtech.erp.core.api.users

import de.jensklingenberg.ktorfit.http.*
import dev.byjtech.erp.core.dto.UserDTO


interface UsersTenantApi {

    @GET("api/core/users/tenant/me")
    suspend fun getUserById(): UserDTO


}