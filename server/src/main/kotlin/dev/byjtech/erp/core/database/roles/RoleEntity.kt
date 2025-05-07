package dev.byjtech.erp.core.database.roles

import dev.byjtech.erp.core.database.companies.CompanyEntity
import dev.byjtech.erp.core.database.users.UserEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RoleEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<dev.byjtech.erp.core.database.roles.RoleEntity>(dev.byjtech.erp.core.database.roles.Roles)

    var name by dev.byjtech.erp.core.database.roles.Roles.name
    var description by dev.byjtech.erp.core.database.roles.Roles.description
    var createdAt by dev.byjtech.erp.core.database.roles.Roles.createdAt
    var createdBy by dev.byjtech.erp.core.database.users.UserEntity.optionalReferencedOn(dev.byjtech.erp.core.database.roles.Roles.createdBy)
    var updatedAt by dev.byjtech.erp.core.database.roles.Roles.updatedAt
    var updatedBy by dev.byjtech.erp.core.database.users.UserEntity.optionalReferencedOn(dev.byjtech.erp.core.database.roles.Roles.updatedBy)
    var company by dev.byjtech.erp.core.database.companies.CompanyEntity referencedOn dev.byjtech.erp.core.database.roles.Roles.companyId
}