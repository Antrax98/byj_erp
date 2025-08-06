package dev.byjtech.erp.document_management.infrastructure.auth

import dev.byjtech.erp.shared.contracts.core.auth.AuthServiceContract
import dev.byjtech.erp.shared.contracts.core.auth.ValidatedSessionInfo
import dev.byjtech.erp.document_management.DocumentManagementDefinition
import dev.byjtech.erp.common.PermissionKey
import io.ktor.server.application.ApplicationCall

class DocumentManagementAuthWrapper(
    private val authService: AuthServiceContract
) {
    fun authorizeOrThrow(
        call: ApplicationCall,
        requiredAnyPermissions: Set<PermissionKey>? = null,
        requiredSuperAdmin: Boolean = false
    ): ValidatedSessionInfo {
        return authService.validateSessionAndAuthorize(
            call = call,
            module = DocumentManagementDefinition.name,
            requiredAnyPermissions = requiredAnyPermissions,
            requiredSuperAdmin = requiredSuperAdmin
        )
    }
}
