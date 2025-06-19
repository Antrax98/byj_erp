package dev.byjtech.erp.core.infrastructure.exposed.tables

import dev.byjtech.erp.core.infrastructure.exposed.tables.BillingsTable.nullable
import dev.byjtech.erp.core.infrastructure.exposed.tables.UserRoleTable.nullable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime


object CompaniesTable : UUIDTable("companies") {
    val name = text("name")
    val rut = text("rut")
    val contactEmail = text("contact_email")
    val createdAt = datetime("created_at").nullable()
    val updatedAt = datetime("updated_at").nullable()
}