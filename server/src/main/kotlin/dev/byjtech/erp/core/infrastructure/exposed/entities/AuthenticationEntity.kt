package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.AuthenticationsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class AuthenticationEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<AuthenticationEntity>(AuthenticationsTable)

    var user by UserEntity referencedOn AuthenticationsTable.userId
    var providerType by AuthenticationsTable.providerType
    var providerKey by AuthenticationsTable.providerKey
    var providerToken by AuthenticationsTable.providerToken
    var hashedPassword by AuthenticationsTable.hashedPassword
    var salt by AuthenticationsTable.salt
    var createdAt by AuthenticationsTable.createdAt
    var updatedAt by AuthenticationsTable.updatedAt
}