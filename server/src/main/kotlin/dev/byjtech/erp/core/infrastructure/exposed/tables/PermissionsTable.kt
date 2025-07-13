package dev.byjtech.erp.core.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable

object PermissionsTable : UUIDTable("permissions") {
    val name = text("name")
    val description = text("description")
    val categoryId = reference("category_id", CategoriesTable)
}