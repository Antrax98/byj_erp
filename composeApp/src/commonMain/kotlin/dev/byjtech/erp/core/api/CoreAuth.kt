package dev.byjtech.erp.core.api

import de.jensklingenberg.ktorfit.http.*
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.response.AccessibleModulesResponse
import dev.byjtech.erp.core.response.PermissionKeysResponse
import dev.byjtech.erp.core.response.PermittedModulesResponse
import dev.byjtech.erp.core.response.SubscribedModulesResponse
import dev.byjtech.erp.core.response.UserPermissionsResponse
import dev.byjtech.erp.core.response.UserRolesResponse

interface CoreAuth {
    @GET("/auth/test")
    suspend fun test(): String

    @GET("/auth/logout")
    suspend fun logout(): String

    @GET("/auth/login")
    suspend fun login(): String

    @GET("/auth/me/type")
    suspend fun getMyType(): String

    @GET("/auth/me")
    suspend fun getMe(): UserDTO


    //podria cambiar estas funciones a que fueran rutas de Subscriptions o Modules??
    @GET("/auth/permitted-modules")
    suspend fun permittedModules(): PermittedModulesResponse

    @GET("/auth/subscribed-modules")
    suspend fun subscribedModules(): SubscribedModulesResponse

    @GET("/auth/accessible-modules")
    suspend fun accessibleModules(): AccessibleModulesResponse

    @GET("/auth/user-permissions")
    suspend fun userPermissions(): UserPermissionsResponse

    //borrar despues
    @GET("/auth/userRoles/{userId}")
    suspend fun userRoles(@Path("userId") userId: Int): UserRolesResponse

    @GET("/auth/userSpecialPermissions/{userId}")
    suspend fun getSpecialPermissionsByUserId(@Path("userId") userId: Int): PermissionKeysResponse
}