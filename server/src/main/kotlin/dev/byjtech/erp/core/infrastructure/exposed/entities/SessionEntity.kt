package dev.byjtech.erp.core.infrastructure.exposed.entities

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import dev.byjtech.erp.core.infrastructure.exposed.tables.SessionsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import java.util.UUID

class SessionEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SessionEntity>(SessionsTable)

    var user by UserEntity referencedOn SessionsTable.userId
    var accessToken by SessionsTable.accessToken
    var refreshToken by SessionsTable.refreshToken
    var isValid by SessionsTable.isValid
    //var sessionId by Sessions.sessionId
    var deviceId by SessionsTable.deviceId
    var userAgent by SessionsTable.userAgent
    var platform by SessionsTable.platform
    var createdAt by SessionsTable.createdAt
    var updatedAt by SessionsTable.updatedAt
    var expiresAt by SessionsTable.expiresAt
}