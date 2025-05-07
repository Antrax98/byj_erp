package dev.byjtech.erp.core.database.permissions

import dev.byjtech.erp.core.database.categories.Categories
import org.jetbrains.exposed.dao.id.IntIdTable

object Permissions : IntIdTable("permissions") {
    val name = text("name")
    val description = text("description")
    val categoryId = reference("category_id", Categories)
}
