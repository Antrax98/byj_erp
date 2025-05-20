package dev.byjtech.erp.core.infrastructure.exposed.tables

import dev.byjtech.erp.core.infrastructure.exposed.tables.BillingsTable.nullable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime


object CompaniesTable : IntIdTable("companies") {
    val name = text("name")
    val contactEmail = text("contact_email")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}