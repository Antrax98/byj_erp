package dev.byjtech.erp.core.database.users

import dev.byjtech.erp.core.database.companies.Companies
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime


object Users : IntIdTable("users") {
    val name = text("name").nullable()
    val email = text("email")
    val googleId = text("google_id").nullable()
    val pictureUrl = text("picture_url").nullable()
    val lastLoginAt = datetime("last_login_at").nullable()
    val isActive = bool("is_active").default(true)
    val createdAt = datetime("created_at").nullable()
    val createdBy = reference("created_by", Users, onDelete = ReferenceOption.SET_NULL).nullable()
    val updatedAt = datetime("updated_at").nullable()
    val updatedBy = reference("updated_by", Users, onDelete = ReferenceOption.SET_NULL).nullable()
    val companyId = reference("company_id", Companies).nullable()
}