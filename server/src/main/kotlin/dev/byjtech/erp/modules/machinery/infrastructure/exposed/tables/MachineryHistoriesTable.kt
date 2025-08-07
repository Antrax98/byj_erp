package dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object MachineryHistoriesTable : UUIDTable("machinery_histories") {
    val machineryId = reference("machinery_id", MachineriesTable)
    val userId = uuid("user_id")
    val field = varchar("field", 50)
    val oldValue = text("old_value").nullable()
    val newValue = text("new_value").nullable()
    val createdAt = datetime("created_at")
    val comment = text("comment").nullable()
}
