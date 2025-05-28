package dev.byjtech.erp.core.infrastructure.exposed.tables

import dev.byjtech.erp.core.infrastructure.exposed.tables.BillingsTable.nullable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object ModulesTable : IntIdTable("modules") {
    val name = text("name")
    val displayName = text("display_name")
    val description = text("description")
    val developerOnly = bool("developer_only").default(false)
}