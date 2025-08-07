package dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities

import dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryHistoriesTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class MachineryHistoryEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MachineryHistoryEntity>(MachineryHistoriesTable)

    var machinery by MachineryEntity referencedOn MachineryHistoriesTable.machineryId
    var userId by MachineryHistoriesTable.userId
    var field by MachineryHistoriesTable.field
    var oldValue by MachineryHistoriesTable.oldValue
    var newValue by MachineryHistoriesTable.newValue
    var createdAt by MachineryHistoriesTable.createdAt
    var comment by MachineryHistoriesTable.comment
}
