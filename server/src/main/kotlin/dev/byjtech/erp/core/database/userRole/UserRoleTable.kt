package dev.byjtech.erp.core.database.userRole

import dev.byjtech.erp.core.database.roles.Roles
import dev.byjtech.erp.core.database.users.Users
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object UserRole : IntIdTable("user_role") {
    val userId = reference("user_id", dev.byjtech.erp.core.database.users.Users)
    val roleId = reference("role_id", dev.byjtech.erp.core.database.roles.Roles)
    val createdAt = datetime("created_at")
    val createdBy = reference("created_by", dev.byjtech.erp.core.database.users.Users, onDelete = ReferenceOption.SET_NULL).nullable()

    init {
        // no se repita la combinación de usuario y rol
        index(isUnique = true,
            dev.byjtech.erp.core.database.userRole.UserRole.userId,
            dev.byjtech.erp.core.database.userRole.UserRole.roleId
        )
    }
}