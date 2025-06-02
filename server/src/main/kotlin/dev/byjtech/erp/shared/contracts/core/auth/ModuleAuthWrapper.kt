package dev.byjtech.erp.shared.contracts.core.auth

import dev.byjtech.erp.common.PermissionKey
import io.ktor.server.application.ApplicationCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//esta clase permite que un modulo la cree y le de su nombre, cosa de que no tenga que dar su nombre en cada call de su metodo de autorizacion
abstract class ModuleAuthWrapper(
    private val authService: AuthServiceContract
) {
    abstract val moduleName: String

    //dejar de usar suspend y dispatcher si hace problemas despues
    suspend fun authorizeOrThrow(call: ApplicationCall, requiredSuperAdmin: Boolean = false, requiredAnyPermissions: Set<PermissionKey>? = null) : ValidatedSessionInfo {
        return withContext(Dispatchers.IO) {
            authService.validateSessionAndAuthorize(call, moduleName, requiredAnyPermissions, requiredSuperAdmin)
        }
    }
}