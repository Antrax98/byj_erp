package dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object MaintenanceSchedulesTable : UUIDTable("maintenance_schedules") {
    val machineryId = reference("machinery_id", MachineriesTable)
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val intervalType = varchar("interval_type", 20) // TIME, HOURS, KILOMETERS
    val intervalValue = integer("interval_value")
    val lastMaintenanceDate = datetime("last_maintenance_date").nullable()
    val nextMaintenanceDate = datetime("next_maintenance_date").nullable()
    val lastOperationalValue = double("last_operational_value").nullable()
    val nextOperationalValue = double("next_operational_value").nullable()
    val createdBy = uuid("created_by")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val isActive = bool("is_active").default(true)
}
