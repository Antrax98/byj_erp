package dev.byjtech.erp.core.domain.model

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class Session(
    val id: UUID,
    val userId: UUID,
    val deviceId: String,
    val accessTokens: String,
    val refreshToken: String,
    val platform: String,
    val userAgent: String?,
    val isValid: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val expiresAt: LocalDateTime
)