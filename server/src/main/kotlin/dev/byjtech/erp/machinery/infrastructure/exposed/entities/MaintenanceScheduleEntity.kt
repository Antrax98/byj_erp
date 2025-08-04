package dev.byjtech.erp.machinery.infrastructure.exposed.entities

import dev.byjtech.erp.machinery.infrastructure.exposed.tables.MaintenanceSchedulesTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class MaintenanceScheduleEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MaintenanceScheduleEntity>(MaintenanceSchedulesTable)

    var machinery by MachineryEntity referencedOn MaintenanceSchedulesTable.machineryId
    var name by MaintenanceSchedulesTable.name
    var description by MaintenanceSchedulesTable.description
    var intervalType by MaintenanceSchedulesTable.intervalType
    var intervalValue by MaintenanceSchedulesTable.intervalValue
    var lastMaintenanceDate by MaintenanceSchedulesTable.lastMaintenanceDate
    var nextMaintenanceDate by MaintenanceSchedulesTable.nextMaintenanceDate
    var lastOperationalValue by MaintenanceSchedulesTable.lastOperationalValue
    var nextOperationalValue by MaintenanceSchedulesTable.nextOperationalValue
    var createdBy by MaintenanceSchedulesTable.createdBy
    var createdAt by MaintenanceSchedulesTable.createdAt
    var updatedAt by MaintenanceSchedulesTable.updatedAt
    var isActive by MaintenanceSchedulesTable.isActive
}
