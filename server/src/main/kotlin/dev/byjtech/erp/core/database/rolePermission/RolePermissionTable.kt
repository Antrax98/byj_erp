package dev.byjtech.erp.core.database.rolePermission

import dev.byjtech.erp.core.database.permissions.Permissions
import dev.byjtech.erp.core.database.roles.Roles
import dev.byjtech.erp.core.database.users.Users
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object RolePermission : IntIdTable("role_permission") {
    val roleId = reference("role_id", Roles)
    val permissionId = reference("permission_id", Permissions)
    val createdAt = datetime("created_at")
    val createdBy = reference("created_by", Users, onDelete = ReferenceOption.SET_NULL).nullable()

    init {
        // un rol no puede tener el mismo permiso 2 veces
        index(true,
            roleId,
            permissionId
        )
    }
}