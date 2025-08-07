package dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object MaintenanceActivitiesTable : IntIdTable("maintenance_activities") {
    val scheduleId = reference("schedule_id", MaintenanceSchedulesTable)
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val estimatedDuration = integer("estimated_duration").nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val isActive = bool("is_active").default(true)
}
