package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.ModulesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ModuleEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ModuleEntity>(ModulesTable)

    var name by ModulesTable.name
    var displayName by ModulesTable.displayName
    var description by ModulesTable.description
    var createdAt by ModulesTable.createdAt
    var updatedAt by ModulesTable.updatedAt
    var developerOnly by ModulesTable.developerOnly
}