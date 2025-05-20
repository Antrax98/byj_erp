package dev.byjtech.erp.core.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserRoleDTO(
    val id: Int,
    val userId: Int,
    val roleId: Int,
)