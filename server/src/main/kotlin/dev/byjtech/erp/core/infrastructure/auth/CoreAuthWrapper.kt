package dev.byjtech.erp.core.infrastructure.auth

import dev.byjtech.erp.shared.contracts.core.auth.ModuleAuthWrapper
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.shared.contracts.core.auth.AuthServiceContract

class CoreAuthWrapper(
    authService: AuthServiceContract
) : ModuleAuthWrapper(authService) {
    override val moduleName : String = CoreDefinition.name
}