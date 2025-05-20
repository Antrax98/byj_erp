package dev.byjtech.erp.shared.contracts.core.auth

import dev.byjtech.erp.common.PermissionKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//esta clase permite que un modulo la cree y le de su nombre, cosa de que no tenga que dar su nombre en cada call de su metodo de autorizacion
abstract class ModuleAuthWrapper(
    private val authService: AuthServiceContract
) {
    abstract val moduleName: String

    //dejar de usar suspend y dispatcher si hace problemas despues
    suspend fun authorizeOrThrow(token: String, requiredSuperAdmin: Boolean = false, requiredAnyPermissions: Set<PermissionKey>? = null) {
        val result = withContext(Dispatchers.IO) {
            authService.validateSessionAndAuthorize(token, moduleName, requiredAnyPermissions, requiredSuperAdmin)
        }
    }
}