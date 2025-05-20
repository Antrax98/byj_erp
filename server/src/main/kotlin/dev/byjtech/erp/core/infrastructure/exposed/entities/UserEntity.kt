package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.UsersTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    var name by UsersTable.name
    var email by UsersTable.email
    var googleId by UsersTable.googleId
    var pictureUrl by UsersTable.pictureUrl
    var isActive by UsersTable.isActive
    var createdAt by UsersTable.createdAt
    var updatedAt by UsersTable.updatedAt
    var company by CompanyEntity.optionalReferencedOn(UsersTable.companyId)
}