package dev.byjtech.erp.core.response

import dev.byjtech.erp.core.dto.ModuleDTO
import kotlinx.serialization.Serializable

@Serializable
data class AccessibleModulesResponse(
    val modules: Set<ModuleDTO>
)
