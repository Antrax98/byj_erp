package dev.byjtech.erp.core.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: String,
    val name: String?,
    val email: String,
    val googleId: String?,
    val pictureUrl: String?,
    val isActive: Boolean,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val companyId: String?
)