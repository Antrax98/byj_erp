package dev.byjtech.erp.machinery.infrastructure.exposed.entities

import dev.byjtech.erp.machinery.infrastructure.exposed.tables.MachineriesTable
import dev.byjtech.erp.core.infrastructure.exposed.entities.CompanyEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class MachineryEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MachineryEntity>(MachineriesTable)

    var code by MachineriesTable.code
    var name by MachineriesTable.name
    var description by MachineriesTable.description
    var brand by MachineriesTable.brand
    var model by MachineriesTable.model
    var year by MachineriesTable.year
    var serialNumber by MachineriesTable.serialNumber
    var licensePlate by MachineriesTable.licensePlate
    var status by MachineriesTable.status
    var location by MachineriesTable.location
    var companyId by MachineriesTable.companyId
    var createdAt by MachineriesTable.createdAt
    var updatedAt by MachineriesTable.updatedAt
    var isActive by MachineriesTable.isActive
}
