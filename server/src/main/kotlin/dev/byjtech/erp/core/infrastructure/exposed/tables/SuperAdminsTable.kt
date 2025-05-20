package dev.byjtech.erp.core.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object SuperAdminsTable : IntIdTable("super_admins") {
    val userId = reference("user_id", UsersTable, onDelete = ReferenceOption.CASCADE) // Hace referencia al usuario
    val description = text("description").nullable()
}