package dev.byjtech.erp.core.infrastructure.exposed.tables

import dev.byjtech.erp.core.infrastructure.exposed.tables.UserRoleTable.nullable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object SubscriptionsTable : UUIDTable("subscription") {
    val companyId = reference("company_id", CompaniesTable)
    val moduleId = reference("module_id", ModulesTable)
    val isActive = bool("is_active").default(true)
    val isAccessible = bool("is_accessible").default(true)
    val createdAt = datetime("created_at").nullable()
    val updatedAt = datetime("updated_at").nullable()

    init {
        index(true,
            companyId,
            moduleId
        )
    }
}