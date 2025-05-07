package dev.byjtech.erp.core.dto.permission

import kotlinx.serialization.Serializable

@Serializable
data class PermissionDTO(
    val id: Int,
    val name: String,
    val description: String,
    val categoryId: Int
)