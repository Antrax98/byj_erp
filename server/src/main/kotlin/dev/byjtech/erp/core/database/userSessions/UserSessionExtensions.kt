package dev.byjtech.erp.core.database.userSessions

import dev.byjtech.erp.core.dto.userSession.UserSessionDTO
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime

fun UserSessionEntity.toDTO(): UserSessionDTO {
    val now = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC)
    return UserSessionDTO(
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