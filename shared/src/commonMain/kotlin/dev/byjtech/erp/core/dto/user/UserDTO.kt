package dev.byjtech.erp.core.dto.user

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val name: String?,
    val email: String,
    val googleId: String?,
    val pictureUrl: String?,
    val lastLoginAt: LocalDateTime?,
    val isActive: Boolean,
    val createdAt: LocalDateTime?,
    val createdBy: Int?,
    val updatedAt: LocalDateTime?,
    val updatedBy: Int?,
    val companyId: Int?
)