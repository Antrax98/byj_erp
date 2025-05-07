package dev.byjtech.erp.core.dto.userRole

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserRoleDTO(
    val id: Int,
    val userId: Int,
    val roleId: Int,
    val createdAt: LocalDateTime,
    val createdBy: Int?
)