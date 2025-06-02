package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.UserPermissionTable
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID

class UserPermissionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object: EntityClass<Int, UserPermissionEntity>(UserPermissionTable)

    var user by UserEntity referencedOn UserPermissionTable.userId
    var permission by PermissionEntity referencedOn UserPermissionTable.permissionId

}