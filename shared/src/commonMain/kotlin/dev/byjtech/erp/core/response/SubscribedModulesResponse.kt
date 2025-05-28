package dev.byjtech.erp.core.response

import dev.byjtech.erp.core.dto.ModuleDTO

data class SubscribedModulesResponse (
    val modules: Set<ModuleDTO>
)