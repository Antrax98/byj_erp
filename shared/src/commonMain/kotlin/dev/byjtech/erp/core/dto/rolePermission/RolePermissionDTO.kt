package dev.byjtech.erp.core.dto.rolePermission

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class RolePermissionDTO(
    val id: Int,
    val roleId: Int,
    val permissionId: Int,
    val createdAt: LocalDateTime,
    val createdBy: Int?
)