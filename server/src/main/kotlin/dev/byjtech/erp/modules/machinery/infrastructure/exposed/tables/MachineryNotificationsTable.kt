package dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object MachineryNotificationsTable : UUIDTable("machinery_notifications") {
    val machineryId = reference("machinery_id", MachineriesTable).nullable()
    val documentId = reference("document_id",
        dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable
    ).nullable()
    val scheduleId = reference("schedule_id", MaintenanceSchedulesTable).nullable()
    val type = varchar("type", 50) // DOCUMENT_EXPIRATION, MAINTENANCE_DUE, etc.
    val title = varchar("title", 255)
    val message = text("message")
    val priority = varchar("priority", 20) // LOW, MEDIUM, HIGH
    val status = varchar("status", 20) // PENDING, READ, ARCHIVED
    val dueDate = datetime("due_date").nullable()
    val notifyUsers = text("notify_users") // JSON array of user IDs to notify
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
