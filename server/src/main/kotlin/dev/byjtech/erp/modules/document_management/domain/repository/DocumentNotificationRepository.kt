package dev.byjtech.erp.document_management.domain.repository

import dev.byjtech.erp.document_management.domain.model.DocumentNotification
import dev.byjtech.erp.document_management.domain.model.NotificationStatus
import dev.byjtech.erp.document_management.domain.model.NotificationType
import java.util.UUID

interface DocumentNotificationRepository {
    fun findById(id: UUID): DocumentNotification?
    fun findByUserId(userId: UUID): List<DocumentNotification>
    fun findPendingByUserId(userId: UUID): List<DocumentNotification>
    fun findByDocumentId(documentId: UUID): List<DocumentNotification>
    fun save(notification: DocumentNotification): DocumentNotification
    fun updateStatus(notificationId: UUID, status: NotificationStatus): DocumentNotification?
    fun findExistingNotification(
        documentId: UUID, 
        userId: UUID, 
        notificationType: NotificationType
    ): DocumentNotification?
    fun countPendingByUserId(userId: UUID): Int
    fun markAsSent(notificationId: UUID): DocumentNotification?
}
