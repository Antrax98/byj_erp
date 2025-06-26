package dev.byjtech.erp.core.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserRoleDTO(
    val id: String,
    val userId: String,
    val roleId: String,
)