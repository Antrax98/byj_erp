package dev.byjtech.erp.core.infrastructure.exposed.tables

import dev.byjtech.erp.core.infrastructure.exposed.tables.BillingsTable.nullable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object RolePermissionTable : IntIdTable("role_permission") {
    val roleId = reference("role_id", RolesTable)
    val permissionId = reference("permission_id", PermissionsTable)
    val createdAt = datetime("created_at").nullable()

    init {
        // un rol no puede tener el mismo permiso 2 veces
        index(true,
            roleId,
            permissionId
        )
    }
}