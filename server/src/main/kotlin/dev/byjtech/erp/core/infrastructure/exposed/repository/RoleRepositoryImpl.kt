package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.domain.model.Role
import dev.byjtech.erp.core.domain.repository.RoleRepository
import dev.byjtech.erp.core.infrastructure.exposed.entities.CompanyEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.PermissionEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.RoleEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.RolePermissionEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserRoleEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.core.infrastructure.exposed.tables.PermissionsTable
import dev.byjtech.erp.core.infrastructure.exposed.tables.RolePermissionTable
import dev.byjtech.erp.core.infrastructure.exposed.tables.RolesTable
import dev.byjtech.erp.core.infrastructure.exposed.tables.UserRoleTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class RoleRepositoryImpl(private val db: Database): RoleRepository {
    override fun create(role: Role): Role {
        return transaction(db) {
            val newRole = RoleEntity.new(UUID.randomUUID()) {
                name = role.name
                description = role.description
                company = CompanyEntity.findById(role.companyId)!!
            }
            return@transaction newRole.toModel()
        }
    }
    override fun getById(roleId: UUID): Role? {
        return transaction(db) {
            val roleEntity = RoleEntity.findById(roleId)
            return@transaction roleEntity?.toModel()
        }
    }
    override fun delete(roleId: UUID) {
        TODO("Not yet implemented")
    }
    override fun findByUserId(userId: UUID): Set<Role> {
        return transaction(db) {
            val userRolesEntities = UserRoleEntity.find { UserRoleTable.userId eq userId }.map { it.role }
            val roles = userRolesEntities.map { it.toModel() }
            return@transaction roles.toSet()
        }
    }
    override fun addPermission(roleId: UUID, permissionId: UUID): Role {
        TODO("Not yet implemented")
    }
    override fun removePermission(roleId: UUID, permissionId: UUID): Role {
        TODO("Not yet implemented")
    }
    override fun getPermissionsByRoleId(roleId: UUID): Set<Permission> {
        return transaction(db) {
            val permissionsEntity = RolePermissionEntity.find { RolePermissionTable.roleId eq roleId }.map { it.permission }
            return@transaction permissionsEntity.map { it.toModel() }.toSet()
        }
    }
    override fun attachPermissions(role: Role): Role {
        return transaction(db) {
            val permissionsEntity = RolePermissionEntity.find { RolePermissionTable.roleId eq role.id }.map { it.permission }
            return@transaction role.copy(permissions = permissionsEntity.map { it.toModel() }.toSet())
        }
    }
}