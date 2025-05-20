package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.RolePermissionTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RolePermissionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RolePermissionEntity>(
        RolePermissionTable
    )

    var role by RoleEntity referencedOn RolePermissionTable.roleId
    var permission by PermissionEntity referencedOn RolePermissionTable.permissionId
    var createdAt by RolePermissionTable.createdAt
}