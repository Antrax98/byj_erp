package dev.byjtech.erp.core.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class RoleDTO(
    val id: Int,
    val name: String,
    val description: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val companyId: Int
)