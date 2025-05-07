package dev.byjtech.erp.core.database.categories

import dev.byjtech.erp.core.database.modules.Modules
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Categories : IntIdTable("categories") {
    val name = text("name")
    val description = text("description")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val moduleId = reference("module_id", Modules)

}