package dev.byjtech.erp.document_management.dto

import dev.byjtech.erp.document_management.domain.model.NotificationStatus
import dev.byjtech.erp.document_management.domain.model.NotificationType
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class DocumentNotificationDTO(
    val id: String,
    val documentId: String,
    val userId: String,
    val notificationType: NotificationType,
    val title: String,
    val message: String,
    val status: NotificationStatus,
    val sentAt: LocalDateTime? = null,
    val createdAt: LocalDateTime
)
