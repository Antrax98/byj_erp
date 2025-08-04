package dev.byjtech.erp.machinery.infrastructure.exposed.entities

import dev.byjtech.erp.machinery.infrastructure.exposed.tables.OperationalDataTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class OperationalDataEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<OperationalDataEntity>(OperationalDataTable)

    var machinery by MachineryEntity referencedOn OperationalDataTable.machineryId
    var type by OperationalDataTable.type
    var value by OperationalDataTable.value
    var recordedAt by OperationalDataTable.recordedAt
    var recordedBy by OperationalDataTable.recordedBy
    var createdAt by OperationalDataTable.createdAt
    var updatedAt by OperationalDataTable.updatedAt
}
