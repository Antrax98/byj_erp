package dev.byjtech.erp.core.database.rolePermission

import dev.byjtech.erp.core.infrastructure.exposed.entities.RolePermissionEntity
import dev.byjtech.erp.core.infrastructure.exposed.tables.UserRoleTable
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserRoleEntity
import dev.byjtech.erp.core.infrastructure.exposed.tables.RolePermissionTable
import dev.byjtech.erp.common.PermissionKey
import org.jetbrains.exposed.sql.transactions.transaction


//TODO: ELIMINAR y reemplazar por un service de roles y/o permisos
//class RolePermissionDataSource{
//    companion object{
//        fun userHasAnyPermission(userId: Int, requiredPermissions: List<PermissionKey>): Boolean {
//            return transaction {
//                val userRoles = UserRoleEntity.find { UserRoleTable.userId eq userId }.map { it.role }
//                val rolePermissions = RolePermissionEntity.find { RolePermissionTable.roleId inList userRoles.map { it.id.value } }.map { it.permission }.distinctBy{it.id.value}
//                val permissionKeysFromRoles = rolePermissions.map {
//                    val category = it.category
//                    val module = category.module
//
//                    PermissionKey(
//                        module = module.name,
//                        category = category.name,
//                        action = it.name
//                    )
//                }.toSet()
//                requiredPermissions.any {it in permissionKeysFromRoles}
//            }
//        }
//    }
//}