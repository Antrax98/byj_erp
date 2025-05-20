package dev.byjtech.erp.core.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class RolePermissionDTO(
    val id: Int,
    val roleId: Int,
    val permissionId: Int,
    val createdAt: LocalDateTime?,
)