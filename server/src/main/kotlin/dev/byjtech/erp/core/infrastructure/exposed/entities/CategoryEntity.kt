package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.CategoriesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CategoryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CategoryEntity>(CategoriesTable)

    var name by CategoriesTable.name
    var description by CategoriesTable.description
    var createdAt by CategoriesTable.createdAt
    var updatedAt by CategoriesTable.updatedAt
    var module by ModuleEntity.referencedOn(CategoriesTable.moduleId)
}