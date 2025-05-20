package dev.byjtech.erp.core.infrastructure.exposed.tables

import dev.byjtech.erp.core.infrastructure.exposed.tables.BillingsTable.nullable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object RolesTable : IntIdTable("roles") {
    val name = text("name")
    val description = text("description")
    val createdAt = datetime("created_at").nullable()
    val updatedAt = datetime("updated_at").nullable()
    val companyId = reference("company_id", CompaniesTable)
}