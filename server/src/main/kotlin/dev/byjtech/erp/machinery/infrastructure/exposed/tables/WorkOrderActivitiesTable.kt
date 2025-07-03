package dev.byjtech.erp.machinery.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object WorkOrderActivitiesTable : UUIDTable("work_order_activities") {
    val workOrderId = reference("work_order_id", WorkOrdersTable)
    val activityId = reference("activity_id", MaintenanceActivitiesTable).nullable()
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val status = varchar("status", 20) // PENDING, COMPLETED, SKIPPED, etc.
    val startDate = datetime("start_date").nullable()
    val completionDate = datetime("completion_date").nullable()
    val comments = text("comments").nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
