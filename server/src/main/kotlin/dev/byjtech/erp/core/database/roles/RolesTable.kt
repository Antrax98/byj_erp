package dev.byjtech.erp.core.database.roles

import dev.byjtech.erp.core.database.users.Users
import dev.byjtech.erp.core.database.companies.Companies
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object Roles : IntIdTable("roles") {
    val name = text("name")
    val description = text("description")
    val createdAt = datetime("created_at")
    val createdBy = reference("created_by", dev.byjtech.erp.core.database.users.Users, onDelete = ReferenceOption.SET_NULL).nullable()
    val updatedAt = datetime("updated_at")
    val updatedBy = reference("updated_by", dev.byjtech.erp.core.database.users.Users, onDelete = ReferenceOption.SET_NULL).nullable()
    val companyId = reference("company_id", dev.byjtech.erp.core.database.companies.Companies)
}