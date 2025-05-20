package dev.byjtech.erp.core.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object SubscriptionsTable : IntIdTable("subscription") {
    val companyId = reference("company_id", CompaniesTable)
    val moduleId = reference("module_id", ModulesTable)
    val isActive = bool("is_active").default(true)
    val isAccessible = bool("is_accessible").default(true)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val billingId = optReference("billing_id", BillingsTable.id)

    init {
        index(true,
            companyId,
            moduleId
        )
    }
}