package dev.byjtech.erp.core.response

import dev.byjtech.erp.core.dto.ModuleDTO
import kotlinx.serialization.Serializable

@Serializable
data class PermittedModulesResponse(
    val modules: Set<ModuleDTO>
)
