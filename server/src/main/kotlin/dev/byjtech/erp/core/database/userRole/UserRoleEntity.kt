package dev.byjtech.erp.core.database.userRole

import dev.byjtech.erp.core.database.roles.RoleEntity
import dev.byjtech.erp.core.database.users.UserEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserRoleEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<dev.byjtech.erp.core.database.userRole.UserRoleEntity>(dev.byjtech.erp.core.database.userRole.UserRole)

    var user by dev.byjtech.erp.core.database.users.UserEntity referencedOn dev.byjtech.erp.core.database.userRole.UserRole.userId
    var role by dev.byjtech.erp.core.database.roles.RoleEntity referencedOn dev.byjtech.erp.core.database.userRole.UserRole.roleId
    var createdAt by dev.byjtech.erp.core.database.userRole.UserRole.createdAt
    var createdBy by dev.byjtech.erp.core.database.users.UserEntity optionalReferencedOn dev.byjtech.erp.core.database.userRole.UserRole.createdBy
}