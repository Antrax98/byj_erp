package dev.byjtech.erp.core.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object SuperAdminsTable : UUIDTable("super_admins") {
    val user = reference("user_id", UsersTable, onDelete = ReferenceOption.CASCADE) // Hace referencia al usuario
    val description = text("description").nullable()
}