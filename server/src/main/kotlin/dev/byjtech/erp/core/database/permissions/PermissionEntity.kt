package dev.byjtech.erp.core.database.permissions

import dev.byjtech.erp.core.database.categories.CategoryEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PermissionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PermissionEntity>(
        Permissions
    )

    var name by Permissions.name
    var description by Permissions.description
    var category by CategoryEntity referencedOn Permissions.categoryId
}