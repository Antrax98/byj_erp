package dev.byjtech.erp.machinery.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object WorkOrdersTable : UUIDTable("work_orders") {
    val machineryId = reference("machinery_id", MachineriesTable)
    val scheduleId = reference("schedule_id", MaintenanceSchedulesTable).nullable()
    val orderNumber = varchar("order_number", 50).uniqueIndex()
    val description = text("description")
    val type = varchar("type", 20) // PREVENTIVE, CORRECTIVE, etc.
    val status = varchar("status", 20) // PENDING, IN_PROGRESS, COMPLETED, etc.
    val scheduledDate = datetime("scheduled_date")
    val startDate = datetime("start_date").nullable()
    val completionDate = datetime("completion_date").nullable()
    val assignedTo = uuid("assigned_to").nullable()
    val createdBy = uuid("created_by")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val comments = text("comments").nullable()
}
