package dev.byjtech.erp.core.database.rolePermission

import dev.byjtech.erp.core.database.permissions.PermissionEntity
import dev.byjtech.erp.core.database.roles.RoleEntity
import dev.byjtech.erp.core.database.users.UserEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RolePermissionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<dev.byjtech.erp.core.database.rolePermission.RolePermissionEntity>(
        dev.byjtech.erp.core.database.rolePermission.RolePermission
    )

    var role by dev.byjtech.erp.core.database.roles.RoleEntity referencedOn dev.byjtech.erp.core.database.rolePermission.RolePermission.roleId
    var permission by dev.byjtech.erp.core.database.permissions.PermissionEntity referencedOn dev.byjtech.erp.core.database.rolePermission.RolePermission.permissionId
    var createdAt by dev.byjtech.erp.core.database.rolePermission.RolePermission.createdAt
    var createdBy by dev.byjtech.erp.core.database.users.UserEntity optionalReferencedOn dev.byjtech.erp.core.database.rolePermission.RolePermission.createdBy
}