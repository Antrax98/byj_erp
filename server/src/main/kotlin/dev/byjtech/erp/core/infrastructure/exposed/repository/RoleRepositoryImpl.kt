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
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
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

    //elimina el rol y todo lo relacionado con este
    override fun deleteEverything(roleId: UUID) {
        return transaction(db) {
            val role = RoleEntity.findById(roleId)!!
            //borra los permisos del rol
            RolePermissionEntity.find { RolePermissionTable.roleId eq roleId }.forEach { it.delete() }
            //borra las relaciones con los usuarios
            UserRoleEntity.find { UserRoleTable.roleId eq roleId }.forEach { it.delete() }
            //borra el rol
            role.delete()
        }

    }
    override fun findByUserId(userId: UUID): Set<Role> {
        return transaction(db) {
            val userRolesEntities = UserRoleEntity.find { UserRoleTable.userId eq userId }.map { it.role }
            val roles = userRolesEntities.map { it.toModel() }
            return@transaction roles.toSet()
        }
    }
    override fun addPermission(roleId: UUID, permissionId: UUID): Role {
        return transaction(db) {
            val roleEntity = RoleEntity.findById(roleId)!!
            val permissionEntity = PermissionEntity.findById(permissionId)!!
            RolePermissionEntity.new {
                role = roleEntity
                this.permission = permissionEntity
            }
            roleEntity.updatedAt = LocalDateTime.now()
            return@transaction roleEntity.toModel()
        }
    }

    override fun addPermissions(roleId: UUID, permissionIds: Set<UUID>): Role {
        return transaction(db) {
            val roleEntity = RoleEntity.findById(roleId)!!
            val permissionsEntity = PermissionEntity.find { PermissionsTable.id inList permissionIds }.toList()
            permissionsEntity.forEach { permission ->
                RolePermissionEntity.new {
                    role = roleEntity
                    this.permission = permission
                    createdAt = LocalDateTime.now()
                }
            }
            roleEntity.updatedAt = LocalDateTime.now()
            return@transaction roleEntity.toModel()
        }
    }
    override fun removePermission(roleId: UUID, permissionId: UUID): Role {
        return transaction(db) {
            //asegurase que el rol y el permiso existen
            val roleEntity = RoleEntity.findById(roleId)!!
            val permissionEntity = PermissionEntity.findById(permissionId)!!
            RolePermissionEntity.find { RolePermissionTable.roleId eq roleId and (RolePermissionTable.permissionId eq permissionId) }.firstOrNull()?.delete()
            roleEntity.updatedAt = LocalDateTime.now()
            return@transaction roleEntity.toModel()
        }
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

    override fun findByCompanyId(companyId: UUID): Set<Role> {
        return transaction(db) {
            val rolesEntities = RoleEntity.find { RolesTable.companyId eq companyId }.map { it.toModel() }
            return@transaction rolesEntities.toSet()
        }

    }

    override fun findByName(name: String): Role? {
        return transaction(db) {
            val roleEntity = RoleEntity.find { RolesTable.name eq name }.firstOrNull()
            return@transaction roleEntity?.toModel()
        }
    }
}