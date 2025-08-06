package dev.byjtech.erp.document_management.infrastructure.exposed.repository

import dev.byjtech.erp.document_management.domain.model.DocumentNotification
import dev.byjtech.erp.document_management.domain.model.NotificationStatus
import dev.byjtech.erp.document_management.domain.model.NotificationType
import dev.byjtech.erp.document_management.domain.repository.DocumentNotificationRepository
import dev.byjtech.erp.document_management.infrastructure.exposed.entities.DocumentNotificationEntity
import dev.byjtech.erp.document_management.infrastructure.exposed.tables.DocumentNotificationsTable
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class DocumentNotificationRepositoryImpl(
    private val database: Database
) : DocumentNotificationRepository {

    override fun findById(id: UUID): DocumentNotification? = transaction(database) {
        DocumentNotificationEntity.findById(id)?.toDomain()
    }

    override fun findByUserId(userId: UUID): List<DocumentNotification> = transaction(database) {
        DocumentNotificationEntity.find { DocumentNotificationsTable.userId eq userId }
            .orderBy(DocumentNotificationsTable.createdAt to org.jetbrains.exposed.sql.SortOrder.DESC)
            .map { it.toDomain() }
    }

    override fun findPendingByUserId(userId: UUID): List<DocumentNotification> = transaction(database) {
        DocumentNotificationEntity.find { 
            (DocumentNotificationsTable.userId eq userId) and 
            (DocumentNotificationsTable.status eq NotificationStatus.PENDING)
        }
        .orderBy(DocumentNotificationsTable.createdAt to org.jetbrains.exposed.sql.SortOrder.DESC)
        .map { it.toDomain() }
    }

    override fun findByDocumentId(documentId: UUID): List<DocumentNotification> = transaction(database) {
        DocumentNotificationEntity.find { DocumentNotificationsTable.documentId eq documentId }
            .map { it.toDomain() }
    }

    override fun save(notification: DocumentNotification): DocumentNotification = transaction(database) {
        val entity = DocumentNotificationEntity.new {
            document = dev.byjtech.erp.document_management.infrastructure.exposed.entities.DocumentEntity[notification.documentId]
            userId = notification.userId
            notificationType = notification.notificationType
            title = notification.title
            message = notification.message
            status = notification.status
            sentAt = notification.sentAt?.toJavaLocalDateTime()
            createdAt = notification.createdAt.toJavaLocalDateTime()
        }
        entity.toDomain()
    }

    override fun updateStatus(notificationId: UUID, status: NotificationStatus): DocumentNotification? = transaction(database) {
        val entity = DocumentNotificationEntity.findById(notificationId)
        entity?.let {
            it.status = status
            if (status == NotificationStatus.READ) {
                it.sentAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
            }
            it.toDomain()
        }
    }

    override fun findExistingNotification(
        documentId: UUID, 
        userId: UUID, 
        notificationType: NotificationType
    ): DocumentNotification? = transaction(database) {
        DocumentNotificationEntity.find { 
            (DocumentNotificationsTable.documentId eq documentId) and
            (DocumentNotificationsTable.userId eq userId) and
            (DocumentNotificationsTable.notificationType eq notificationType) and
            (DocumentNotificationsTable.status eq NotificationStatus.PENDING)
        }.singleOrNull()?.toDomain()
    }

    override fun countPendingByUserId(userId: UUID): Int = transaction(database) {
        DocumentNotificationEntity.find { 
            (DocumentNotificationsTable.userId eq userId) and 
            (DocumentNotificationsTable.status eq NotificationStatus.PENDING)
        }.count().toInt()
    }

    override fun markAsSent(notificationId: UUID): DocumentNotification? = transaction(database) {
        val entity = DocumentNotificationEntity.findById(notificationId)
        entity?.let {
            it.sentAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
            it.toDomain()
        }
    }
}
