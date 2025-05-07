package dev.byjtech.erp.core.database.modules

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Modules : IntIdTable("modules") {
    val name = text("name")
    val displayName = text("display_name")
    val description = text("description")
    val version = text("version")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val developerOnly = bool("developer_only").default(false)
}