package dev.byjtech.erp.core.infrastructure.exposed.tables

import dev.byjtech.erp.core.infrastructure.exposed.tables.BillingsTable.nullable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object CategoriesTable : IntIdTable("categories") {
    val name = text("name")
    val description = text("description")
    val moduleId = reference("module_id", ModulesTable)
}