package dev.byjtech.erp.core.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object UserPermissionTable: IntIdTable("user_permission") {
    val userId = reference("user_id", UsersTable)
    val permissionId = reference("permission_id", PermissionsTable)

    init {
        index(isUnique = true,
            userId,
            permissionId
        )
    }
}