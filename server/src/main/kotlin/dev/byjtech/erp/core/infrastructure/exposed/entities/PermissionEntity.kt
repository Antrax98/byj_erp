package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.PermissionsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PermissionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PermissionEntity>(PermissionsTable)

    var name by PermissionsTable.name
    var description by PermissionsTable.description
    var category by CategoryEntity referencedOn PermissionsTable.categoryId
}