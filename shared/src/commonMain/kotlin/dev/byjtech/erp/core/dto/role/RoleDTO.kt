package dev.byjtech.erp.core.dto.role

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class RoleDTO(
    val id: Int,
    val name: String,
    val description: String,
    val createdAt: LocalDateTime,
    val createdBy: Int?,
    val updatedAt: LocalDateTime,
    val updatedBy: Int?,
    val companyId: Int
)