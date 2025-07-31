package dev.byjtech.erp.document_management.domain.model

import kotlinx.datetime.LocalDateTime
import java.util.UUID

/**
 * Configuraciones de notificación por usuario
 * Permite al usuario configurar cuántos días antes del vencimiento quiere recibir notificaciones
 */
data class UserNotificationSettings(
    val id: UUID,
    val userId: UUID,
    val companyId: UUID,
    val daysBeforeExpiration: Int = 7, // Por defecto 7 días antes
    val emailEnabled: Boolean = true,
    val systemEnabled: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
