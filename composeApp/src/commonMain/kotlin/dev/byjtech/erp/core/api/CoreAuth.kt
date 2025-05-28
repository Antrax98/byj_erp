package dev.byjtech.erp.core.api

import de.jensklingenberg.ktorfit.http.*
import dev.byjtech.erp.core.response.AccessibleModulesResponse
import dev.byjtech.erp.core.response.PermittedModulesResponse
import dev.byjtech.erp.core.response.SubscribedModulesResponse

interface CoreAuth {
    @GET("/auth/test")
    suspend fun test(): String

    @GET("/auth/logout")
    suspend fun logout(): String

    @GET("/auth/login")
    suspend fun login(): String


    //podria cambiar estas funciones a que fueran rutas de Subscriptions o Modules??
    @GET("/auth/permitted-modules")
    suspend fun permittedModules(): PermittedModulesResponse

    @GET("/auth/subscribed-modules")
    suspend fun subscribedModules(): SubscribedModulesResponse

    @GET("/auth/accessible-modules")
    suspend fun accessibleModules(): AccessibleModulesResponse

}