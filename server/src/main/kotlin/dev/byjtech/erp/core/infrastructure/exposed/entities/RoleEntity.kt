package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.RolesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RoleEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoleEntity>(RolesTable)

    var name by RolesTable.name
    var description by RolesTable.description
    var createdAt by RolesTable.createdAt
    var updatedAt by RolesTable.updatedAt
    var company by CompanyEntity referencedOn RolesTable.companyId
}