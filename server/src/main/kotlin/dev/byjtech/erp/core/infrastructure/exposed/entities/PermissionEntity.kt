package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.PermissionsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class PermissionEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PermissionEntity>(PermissionsTable)

    var name by PermissionsTable.name
    var description by PermissionsTable.description
}