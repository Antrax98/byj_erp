package dev.byjtech.erp.core.database.companies

import dev.byjtech.erp.core.database.users.UserEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CompanyEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<dev.byjtech.erp.core.database.companies.CompanyEntity>(dev.byjtech.erp.core.database.companies.Companies)

    var name by dev.byjtech.erp.core.database.companies.Companies.name
    var contactEmail by dev.byjtech.erp.core.database.companies.Companies.contactEmail
    var createdAt by dev.byjtech.erp.core.database.companies.Companies.createdAt
    var updatedAt by dev.byjtech.erp.core.database.companies.Companies.updatedAt
}