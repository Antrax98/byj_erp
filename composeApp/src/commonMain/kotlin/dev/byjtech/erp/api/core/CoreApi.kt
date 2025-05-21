package dev.byjtech.erp.api.core

import de.jensklingenberg.ktorfit.http.*

interface CoreApi {

    //opciones "superadmin" o "tenant"
    @GET("api/core/users/me/type")
    suspend fun getMyType(): String

}