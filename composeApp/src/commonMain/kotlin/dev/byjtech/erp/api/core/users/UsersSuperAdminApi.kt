package dev.byjtech.erp.api.core.users

import de.jensklingenberg.ktorfit.http.*
import dev.byjtech.erp.core.dto.user.UserDTO

interface UsersSuperAdminApi {

    @GET("api/core/core/users/superadmin/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserDTO

}