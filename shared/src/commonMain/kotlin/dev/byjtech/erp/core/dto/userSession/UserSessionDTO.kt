package dev.byjtech.erp.core.dto.userSession

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserSessionDTO(
    val id: Int,
    val userId: Int,
    val deviceId: String,
    val platform: String,
    val userAgent: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val expiresAt: LocalDateTime,
    val isValid: Boolean,
    val isActive: Boolean
)