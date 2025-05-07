package dev.byjtech.erp.api.core

import de.jensklingenberg.ktorfit.http.*

interface CoreAuth {
    @GET("/auth/test")
    suspend fun test(): String

    @GET("/auth/logout")
    suspend fun logout(): String

    @GET("/auth/login")
    suspend fun login(): String
}