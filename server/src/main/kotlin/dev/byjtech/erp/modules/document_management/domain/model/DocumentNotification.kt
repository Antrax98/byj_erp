package dev.byjtech.erp.document_management.domain.model

import kotlinx.datetime.LocalDateTime
import java.util.UUID

/**
 * Notificación generada para un documento
 */
data class DocumentNotification(
    val id: UUID,
    val documentId: UUID,
    val userId: UUID,
    val notificationType: NotificationType,
    val title: String,
    val message: String,
    val status: NotificationStatus = NotificationStatus.PENDING,
    val sentAt: LocalDateTime? = null,
    val createdAt: LocalDateTime
)
