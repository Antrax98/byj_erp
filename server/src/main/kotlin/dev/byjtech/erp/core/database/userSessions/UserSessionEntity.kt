package dev.byjtech.erp.core.database.userSessions

import dev.byjtech.erp.core.database.users.UserEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserSessionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserSessionEntity>(UserSessions)

    var user by UserEntity referencedOn UserSessions.userId
    var accessToken by UserSessions.accessToken
    var refreshToken by UserSessions.refreshToken
    var isValid by UserSessions.isValid
    //var sessionId by UserSessions.sessionId
    var deviceId by UserSessions.deviceId
    var userAgent by UserSessions.userAgent
    var platform by UserSessions.platform
    var createdAt by UserSessions.createdAt
    var updatedAt by UserSessions.updatedAt
    var expiresAt by UserSessions.expiresAt
}