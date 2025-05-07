package dev.byjtech.erp.core.database.superAdmins

import dev.byjtech.erp.core.database.users.Users
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object SuperAdmins : IntIdTable("super_admins") {
    val userId = reference("user_id", Users, onDelete = ReferenceOption.CASCADE) // Hace referencia al usuario
    val description = text("description").nullable()
}