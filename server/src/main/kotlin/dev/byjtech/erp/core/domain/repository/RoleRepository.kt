package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.domain.model.Role
import java.util.UUID

interface RoleRepository {
    fun create(role: Role): Role
    fun getById(roleId: UUID): Role?
    fun deleteEverything(roleId: UUID)
    fun findByUserId(userId: UUID): Set<Role>
    fun addPermission(roleId: UUID, permissionId: UUID): Role
    fun addPermissions(roleId: UUID, permissionIds: Set<UUID>): Role
    fun removePermission(roleId: UUID, permissionId: UUID): Role
    fun getPermissionsByRoleId(roleId: UUID): Set<Permission>
    fun attachPermissions(role:Role): Role
    fun findByCompanyId(companyId: UUID): Set<Role>
    fun findByName(name: String): Role?
}