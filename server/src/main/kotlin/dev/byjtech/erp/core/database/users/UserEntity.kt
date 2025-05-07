package dev.byjtech.erp.core.database.users

import dev.byjtech.erp.core.database.companies.CompanyEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(Users)

    var name by Users.name
    var email by Users.email
    var googleId by Users.googleId
    var pictureUrl by Users.pictureUrl
    var lastLoginAt by Users.lastLoginAt
    var isActive by Users.isActive
    var createdAt by Users.createdAt
    var createdBy by UserEntity.optionalReferencedOn(Users.createdBy)
    var updatedAt by Users.updatedAt
    var updatedBy by UserEntity.optionalReferencedOn(Users.updatedBy)
    var company by CompanyEntity.optionalReferencedOn(Users.companyId)
}