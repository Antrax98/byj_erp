package dev.byjtech.erp.core.database.categories

import dev.byjtech.erp.core.database.modules.ModuleEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDateTime

class CategoryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CategoryEntity>(Categories)

    var name by Categories.name
    var description by Categories.description
    var createdAt by Categories.createdAt
    var updatedAt by Categories.updatedAt
    var module by ModuleEntity.referencedOn(Categories.moduleId)
}