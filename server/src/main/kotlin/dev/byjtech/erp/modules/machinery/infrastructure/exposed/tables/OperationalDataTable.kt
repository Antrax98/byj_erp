package dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object OperationalDataTable : UUIDTable("operational_data") {
    val machineryId = reference("machinery_id", MachineriesTable)
    val type = varchar("type", 20) // HOURS, KILOMETERS, etc.
    val value = double("value")
    val recordedAt = datetime("recorded_at")
    val recordedBy = uuid("recorded_by")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
