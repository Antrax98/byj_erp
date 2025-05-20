package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.CompaniesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CompanyEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CompanyEntity>(CompaniesTable)

    var name by CompaniesTable.name
    var contactEmail by CompaniesTable.contactEmail
    var createdAt by CompaniesTable.createdAt
    var updatedAt by CompaniesTable.updatedAt
}