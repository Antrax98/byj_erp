package dev.byjtech.erp.core.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class RolePermissionDTO(
    val id: String,
    val roleId: String,
    val permissionId: String,
    val createdAt: LocalDateTime?,
)