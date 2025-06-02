package dev.byjtech.erp.core.domain.model

import kotlinx.datetime.LocalDateTime

data class Session(
    val id: Int,
    val userId: Int,
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