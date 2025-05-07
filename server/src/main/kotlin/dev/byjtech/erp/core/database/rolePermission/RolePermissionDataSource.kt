package dev.byjtech.erp.core.database.rolePermission

import dev.byjtech.erp.core.database.userRole.UserRole
import dev.byjtech.erp.core.database.userRole.UserRoleEntity
import dev.byjtech.erp.core.permissions.PermissionKey
import org.jetbrains.exposed.sql.transactions.transaction

class RolePermissionDataSource{
    companion object{
        fun userHasAnyPermission(userId: Int, requiredPermissions: List<PermissionKey>): Boolean {
            return transaction {
                val userRoles = UserRoleEntity.find { UserRole.userId eq userId }.map { it.role }
                val rolePermissions = RolePermissionEntity.find { RolePermission.roleId inList userRoles.map { it.id.value } }.map { it.permission }.distinctBy{it.id.value}
                val permissionKeysFromRoles = rolePermissions.map {
                    val category = it.category
                    val module = category.module

                    PermissionKey(
                        module = module.name,
                        version = module.version,
                        category = category.name,
                        action = it.name
                    )
                }.toSet()
                requiredPermissions.any {it in permissionKeysFromRoles}
            }
        }
    }
}