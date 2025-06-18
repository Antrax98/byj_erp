package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.UserPermissionTable
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class UserPermissionEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<UserPermissionEntity>(UserPermissionTable)

    var user by UserEntity referencedOn UserPermissionTable.userId
    var permission by PermissionEntity referencedOn UserPermissionTable.permissionId

}