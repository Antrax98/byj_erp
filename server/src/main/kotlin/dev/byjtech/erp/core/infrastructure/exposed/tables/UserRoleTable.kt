package dev.byjtech.erp.core.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object UserRoleTable : IntIdTable("user_role") {
    val userId = reference("user_id", UsersTable)
    val roleId = reference("role_id", RolesTable)
    val createdAt = datetime("created_at")

    init {
        // no se repita la combinación de usuario y rol
        index(isUnique = true,
            userId,
            roleId
        )
    }
}