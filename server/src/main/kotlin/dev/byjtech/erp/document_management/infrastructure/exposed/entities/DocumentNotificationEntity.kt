package dev.byjtech.erp.document_management.infrastructure.exposed.entities

import dev.byjtech.erp.document_management.domain.model.DocumentNotification
import dev.byjtech.erp.document_management.infrastructure.exposed.tables.DocumentNotificationsTable
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class DocumentNotificationEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<DocumentNotificationEntity>(DocumentNotificationsTable)

    var document by DocumentEntity referencedOn DocumentNotificationsTable.documentId
    var userId by DocumentNotificationsTable.userId
    var notificationType by DocumentNotificationsTable.notificationType
    var title by DocumentNotificationsTable.title
    var message by DocumentNotificationsTable.message
    var status by DocumentNotificationsTable.status
    var sentAt by DocumentNotificationsTable.sentAt
    var createdAt by DocumentNotificationsTable.createdAt

    fun toDomain() = DocumentNotification(
        id = id.value,
        documentId = document.id.value,
        userId = userId,
        notificationType = notificationType,
        title = title,
        message = message,
        status = status,
        sentAt = sentAt?.toKotlinLocalDateTime(),
        createdAt = createdAt.toKotlinLocalDateTime()
    )
}
