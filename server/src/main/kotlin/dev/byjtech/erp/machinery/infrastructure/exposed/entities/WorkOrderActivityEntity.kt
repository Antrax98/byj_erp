package dev.byjtech.erp.machinery.infrastructure.exposed.entities

import dev.byjtech.erp.machinery.infrastructure.exposed.tables.WorkOrderActivitiesTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class WorkOrderActivityEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<WorkOrderActivityEntity>(WorkOrderActivitiesTable)

    var workOrder by WorkOrderEntity referencedOn WorkOrderActivitiesTable.workOrderId
    var activity by MaintenanceActivityEntity optionalReferencedOn WorkOrderActivitiesTable.activityId
    var name by WorkOrderActivitiesTable.name
    var description by WorkOrderActivitiesTable.description
    var status by WorkOrderActivitiesTable.status
    var startDate by WorkOrderActivitiesTable.startDate
    var completionDate by WorkOrderActivitiesTable.completionDate
    var comments by WorkOrderActivitiesTable.comments
    var createdAt by WorkOrderActivitiesTable.createdAt
    var updatedAt by WorkOrderActivitiesTable.updatedAt
}
