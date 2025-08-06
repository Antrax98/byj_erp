package dev.byjtech.erp.document_management.infrastructure.exposed.tables

import dev.byjtech.erp.document_management.infrastructure.exposed.columns.NotificationTypeColumnType
import dev.byjtech.erp.document_management.infrastructure.exposed.columns.NotificationStatusColumnType
import dev.byjtech.erp.document_management.domain.model.NotificationType
import dev.byjtech.erp.document_management.domain.model.NotificationStatus
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object DocumentNotificationsTable : UUIDTable("document_notifications") {
    val documentId = reference("document_id", DocumentsTable)
    val userId = uuid("user_id") // referencia a users en core DB
    val notificationType = registerColumn<NotificationType>("notification_type", NotificationTypeColumnType())
    val title = varchar("title", 255)
    val message = text("message")
    val status = registerColumn<NotificationStatus>("status", NotificationStatusColumnType())
    val sentAt = datetime("sent_at").nullable()
    val createdAt = datetime("created_at")

    init {
        index(false, userId, status)
        index(false, documentId)
    }
}
