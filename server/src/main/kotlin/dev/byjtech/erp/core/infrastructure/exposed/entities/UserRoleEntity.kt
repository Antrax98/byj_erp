package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.UserRoleTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserRoleEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserRoleEntity>(UserRoleTable)

    var user by UserEntity referencedOn UserRoleTable.userId
    var role by RoleEntity referencedOn UserRoleTable.roleId
    var createdAt by UserRoleTable.createdAt
}