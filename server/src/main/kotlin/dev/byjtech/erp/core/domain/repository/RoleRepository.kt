package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.domain.model.Role

interface RoleRepository {
    fun create(role: Role): Role
    fun getById(roleId: Int): Role?
    fun delete(roleId: Int)
    fun findByUserId(userId: Int): Set<Role>
    fun addPermission(roleId: Int, permissionId: Int): Role
    fun removePermission(roleId: Int, permissionId: Int): Role
    fun getPermissionsByRoleId(roleId: Int): Set<Permission>
    fun attachPermissions(role:Role): Role
}