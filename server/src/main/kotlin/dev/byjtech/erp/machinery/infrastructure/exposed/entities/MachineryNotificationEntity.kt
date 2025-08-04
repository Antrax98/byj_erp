package dev.byjtech.erp.machinery.infrastructure.exposed.entities

import dev.byjtech.erp.machinery.infrastructure.exposed.tables.MachineryNotificationsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class MachineryNotificationEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MachineryNotificationEntity>(MachineryNotificationsTable)

    var machinery by MachineryEntity optionalReferencedOn MachineryNotificationsTable.machineryId
    var document by MachineryDocumentEntity optionalReferencedOn MachineryNotificationsTable.documentId
    var schedule by MaintenanceScheduleEntity optionalReferencedOn MachineryNotificationsTable.scheduleId
    var type by MachineryNotificationsTable.type
    var title by MachineryNotificationsTable.title
    var message by MachineryNotificationsTable.message
    var priority by MachineryNotificationsTable.priority
    var status by MachineryNotificationsTable.status
    var dueDate by MachineryNotificationsTable.dueDate
    var notifyUsers by MachineryNotificationsTable.notifyUsers
    var createdAt by MachineryNotificationsTable.createdAt
    var updatedAt by MachineryNotificationsTable.updatedAt
}
