package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.domain.model.Session
import dev.byjtech.erp.core.dto.SessionDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.SessionEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime

fun SessionEntity.toDTO(): SessionDTO {
    val now = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC)
    return SessionDTO(
        id = this.id.value,
        userId = this.user.id.value,
        deviceId = this.deviceId,
        platform = this.platform,
        createdAt = this.createdAt.toKotlinx(),
        updatedAt = this.updatedAt.toKotlinx(),
        expiresAt = this.expiresAt.toKotlinx(),
        userAgent = this.userAgent,
        isValid = this.isValid,
        isActive = this.isValid && this.expiresAt.toKotlinLocalDateTime() > now
    )
}

fun SessionEntity.toModel(): Session {
    return Session(
        id = this.id.value,
        userId = this.user.id.value,
        deviceId = this.deviceId,
        platform = this.platform,
        createdAt = this.createdAt.toKotlinLocalDateTime(),
        updatedAt = this.updatedAt.toKotlinLocalDateTime(),
        expiresAt = this.expiresAt.toKotlinLocalDateTime(),
        userAgent = this.userAgent,
        isValid = this.isValid,
        accessTokens = this.accessToken,
        refreshToken = this.refreshToken
    )
}