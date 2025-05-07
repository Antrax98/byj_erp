package dev.byjtech.erp.core.dto.companyModule

import kotlinx.serialization.Serializable

@Serializable
data class CompanyModuleDTO(
    val id: Int,
    val companyId: Int,
    val moduleId: Int,
    val isActive: Boolean,
    val isAccessible: Boolean
)