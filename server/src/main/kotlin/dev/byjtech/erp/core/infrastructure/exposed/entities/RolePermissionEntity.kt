package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.RolePermissionTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class RolePermissionEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RolePermissionEntity>(
        RolePermissionTable
    )

    var role by RoleEntity referencedOn RolePermissionTable.roleId
    var permission by PermissionEntity referencedOn RolePermissionTable.permissionId
    var createdAt by RolePermissionTable.createdAt
}