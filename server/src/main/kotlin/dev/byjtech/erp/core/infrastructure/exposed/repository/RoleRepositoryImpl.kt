package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.domain.model.Role
import dev.byjtech.erp.core.domain.repository.RoleRepository
import dev.byjtech.erp.core.infrastructure.exposed.entities.PermissionEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.RoleEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.RolePermissionEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserRoleEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.core.infrastructure.exposed.tables.PermissionsTable
import dev.byjtech.erp.core.infrastructure.exposed.tables.RolePermissionTable
import dev.byjtech.erp.core.infrastructure.exposed.tables.RolesTable
import dev.byjtech.erp.core.infrastructure.exposed.tables.UserRoleTable
import org.jetbrains.exposed.sql.transactions.transaction

class RoleRepositoryImpl: RoleRepository {
    override fun create(role: Role): Role {
        TODO("Not yet implemented")
    }
    override fun getById(roleId: Int): Role? {
        TODO("Not yet implemented")
    }
    override fun delete(roleId: Int) {
        TODO("Not yet implemented")
    }
    override fun findByUserId(userId: Int): Set<Role> {
        return transaction {
            val userRolesEntities = UserRoleEntity.find { UserRoleTable.userId eq userId }.map { it.role }
            val roles = userRolesEntities.map { it.toModel() }
            return@transaction roles.toSet()
        }
    }
    override fun addPermission(roleId: Int, permissionId: Int): Role {
        TODO("Not yet implemented")
    }
    override fun removePermission(roleId: Int, permissionId: Int): Role {
        TODO("Not yet implemented")
    }
    override fun getPermissionsByRoleId(roleId: Int): Set<Permission> {
        return transaction {
            val permissionsEntity = RolePermissionEntity.find { RolePermissionTable.roleId eq roleId }.map { it.permission }
            return@transaction permissionsEntity.map { it.toModel() }.toSet()
        }
    }
    override fun attachPermissions(role: Role): Role {
        return transaction {
            val permissionsEntity = RolePermissionEntity.find { RolePermissionTable.roleId eq role.id }.map { it.permission }
            return@transaction role.copy(permissions = permissionsEntity.map { it.toModel() }.toSet())
        }
    }
}