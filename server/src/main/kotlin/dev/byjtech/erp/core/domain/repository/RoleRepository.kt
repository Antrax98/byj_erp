package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.core.domain.model.Role

interface RoleRepository {
    fun create(role: Role): Role
    fun getById(roleId: Int): Role?
    fun delete(roleId: Int)
    fun addPermission(roleId: Int, permissionId: Int): Role
    fun removePermission(roleId: Int, permissionId: Int): Role
}