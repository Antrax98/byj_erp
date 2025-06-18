package dev.byjtech.erp.core.api.users

import de.jensklingenberg.ktorfit.http.*
import dev.byjtech.erp.core.dto.UserDTO

interface UsersSuperAdminApi {

    @GET("api/core/users/super-admin/{id}")
    suspend fun getUserById(@Path("id") id: String): UserDTO

}