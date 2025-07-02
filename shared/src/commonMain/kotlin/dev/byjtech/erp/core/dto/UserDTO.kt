package dev.byjtech.erp.core.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: String = "",
    val name: String? = null,
    val email: String,
    val googleId: String? = null,
    val pictureUrl: String? = null,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val companyId: String? = null
)