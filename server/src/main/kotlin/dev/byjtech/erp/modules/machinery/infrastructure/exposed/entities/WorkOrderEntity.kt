package dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities

import dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.WorkOrdersTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class WorkOrderEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<WorkOrderEntity>(WorkOrdersTable)

    var machinery by MachineryEntity referencedOn WorkOrdersTable.machineryId
    var schedule by MaintenanceScheduleEntity optionalReferencedOn WorkOrdersTable.scheduleId
    var orderNumber by WorkOrdersTable.orderNumber
    var description by WorkOrdersTable.description
    var type by WorkOrdersTable.type
    var status by WorkOrdersTable.status
    var scheduledDate by WorkOrdersTable.scheduledDate
    var startDate by WorkOrdersTable.startDate
    var completionDate by WorkOrdersTable.completionDate
    var assignedTo by WorkOrdersTable.assignedTo
    var createdBy by WorkOrdersTable.createdBy
    var createdAt by WorkOrdersTable.createdAt
    var updatedAt by WorkOrdersTable.updatedAt
    var comments by WorkOrdersTable.comments
}
