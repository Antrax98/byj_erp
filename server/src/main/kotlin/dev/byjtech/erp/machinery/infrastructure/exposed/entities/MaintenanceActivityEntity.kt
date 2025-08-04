package dev.byjtech.erp.machinery.infrastructure.exposed.entities

import dev.byjtech.erp.machinery.infrastructure.exposed.tables.MaintenanceActivitiesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class MaintenanceActivityEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MaintenanceActivityEntity>(MaintenanceActivitiesTable)

    var schedule by MaintenanceScheduleEntity referencedOn MaintenanceActivitiesTable.scheduleId
    var name by MaintenanceActivitiesTable.name
    var description by MaintenanceActivitiesTable.description
    var estimatedDuration by MaintenanceActivitiesTable.estimatedDuration
    var createdAt by MaintenanceActivitiesTable.createdAt
    var updatedAt by MaintenanceActivitiesTable.updatedAt
    var isActive by MaintenanceActivitiesTable.isActive
}
