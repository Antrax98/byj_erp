package dev.byjtech.erp.core.database.modules

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ModuleEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<dev.byjtech.erp.core.database.modules.ModuleEntity>(dev.byjtech.erp.core.database.modules.Modules)

    var name by dev.byjtech.erp.core.database.modules.Modules.name
    var displayName by dev.byjtech.erp.core.database.modules.Modules.displayName
    var description by dev.byjtech.erp.core.database.modules.Modules.description
    var version by dev.byjtech.erp.core.database.modules.Modules.version
    var createdAt by dev.byjtech.erp.core.database.modules.Modules.createdAt
    var updatedAt by dev.byjtech.erp.core.database.modules.Modules.updatedAt
    var developerOnly by dev.byjtech.erp.core.database.modules.Modules.developerOnly
}