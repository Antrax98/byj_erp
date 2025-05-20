package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.SuperAdminsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SuperAdminEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SuperAdminEntity>(SuperAdminsTable)

    var userId by SuperAdminsTable.userId
    var description by SuperAdminsTable.description // Descripción del superadmin
}