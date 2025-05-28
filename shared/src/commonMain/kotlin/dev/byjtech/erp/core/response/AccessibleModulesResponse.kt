package dev.byjtech.erp.core.response

import dev.byjtech.erp.core.dto.ModuleDTO

data class AccessibleModulesResponse(
    val modules: Set<ModuleDTO>
)
