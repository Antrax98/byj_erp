package dev.byjtech.erp.core.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime


object UsersTable : UUIDTable("users") {
    val name = text("name").nullable()
    val email = text("email").default("")
    //val googleId = text("google_id").nullable()
    val pictureUrl = text("picture_url").nullable()
    val isActive = bool("is_active").default(true)
    val createdAt = datetime("created_at").nullable()
    val updatedAt = datetime("updated_at").nullable()
    val companyId = reference("company_id", CompaniesTable).nullable()
}