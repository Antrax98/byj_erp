package dev.byjtech.erp.core.database.userSessions

import dev.byjtech.erp.config.logger
import dev.byjtech.erp.core.database.users.UserEntity
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.Instant


class UserSessionsDataSource {

    // Crear o reemplazar una sesión de usuario


//    fun findSessionByUserIdAndDeviceId(userId: Int, deviceId: String): UserSessionEntity? = transaction {
//        UserSessionEntity.find {
//            (UserSessions.userId eq userId) and
//                    (UserSessions.deviceId eq deviceId) and
//                    (UserSessions.isValid eq true)
//        }.singleOrNull()
//    }



    //REEMPLPAZADA POR updateSession() y deleteSessionById()
//    // Actualizar el accessToken y refreshToken de una sesión existente
//    fun refreshSession(sessionId: UUID, newAccessToken: String, newRefreshToken: String) {
//        transaction {
//            val session = UserSessionEntity.find { UserSessions.sessionId eq sessionId }.firstOrNull()
//            session?.let {
//                it.accessToken = newAccessToken
//                it.refreshToken = newRefreshToken
//                it.updatedAt = LocalDateTime.now()  // Actualizamos la fecha de actualización
//            }
//        }
//    }
//
//    // Marcar la sesión como no válida (para hacer logout)
//    fun invalidateSession(sessionEntityId: Int) {
//        transaction {
//            val session = UserSessionEntity.find { UserSessions.sessionId eq sessionId }.firstOrNull()
//            session?.let {
//                it.isValid = false
//            }
//        }
//    }

    //modificar en donde se use para que sea un companion object, tambien las demas arriba


    companion object {
        fun findUserIdBySessionId(sessionId: Int): Int? = transaction {
            val session = UserSessionEntity.findById(sessionId)
            session?.user?.id?.value
        }

        fun createOrReplaceSession(
            userId: Int,
            accessToken: String,
            refreshToken: String,
            userAgent: String,
            deviceId: String,
            platform: String,
            expiresAt: Instant
        ): Int {
            return transaction {
                UserSessionEntity.find {
                    (UserSessions.userId eq userId) and
                            (UserSessions.deviceId eq deviceId) and
                            (UserSessions.isValid eq true)
                }.forEach {
                    it.isValid = false
                }

                val newSession = UserSessionEntity.new {
                    this.user = UserEntity[userId]
                    this.accessToken = accessToken
                    this.refreshToken = refreshToken
                    this.deviceId = deviceId
                    this.platform = platform
                    this.userAgent = userAgent
                    this.createdAt = LocalDateTime.now()
                    this.updatedAt = LocalDateTime.now()
                    this.expiresAt = LocalDateTime.ofInstant(expiresAt, java.time.ZoneId.systemDefault())
                    this.isValid = true
                }

                newSession.id.value
            }
        }

        // Crear una nueva sesión de usuario
        fun createSession(userId: Int, accessToken: String, refreshToken: String, userAgent: String, deviceId: String, platform: String, expiresAt: Instant): UserSessionEntity { // Añadido
            return transaction {
                UserSessionEntity.new {
                    this.user = UserEntity[userId]
                    this.accessToken = accessToken
                    this.refreshToken = refreshToken
                    this.deviceId = deviceId
                    this.platform = platform
                    this.userAgent = userAgent
                    this.createdAt = LocalDateTime.now()
                    this.updatedAt = LocalDateTime.now()
                    this.expiresAt = LocalDateTime.ofInstant(expiresAt, java.time.ZoneId.systemDefault()) // Convertir a LocalDateTime
                    this.isValid = true
                }
            }
        }
        fun findSessionByUserIdAndDeviceId(userId: Int, deviceId: String): UserSessionEntity? = transaction {
            addLogger(StdOutSqlLogger) // Enable SQL logging for this transaction
            logger.debug("Finding session for userId: $userId, deviceId: $deviceId")
            val query = UserSessionEntity.find {
                (UserSessions.userId eq userId) and
                        (UserSessions.deviceId eq deviceId) and
                        (UserSessions.isValid eq true)
            }
                .orderBy(UserSessions.updatedAt to SortOrder.DESC)
                .limit(1)
            val session = query.firstOrNull()
            logger.debug("Found session: $session")
            session
        }

        fun updateSession(sessionId: Int, accessToken: String, refreshToken: String, userAgent: String, platform: String, expiresAt: Instant) = transaction { // Añadido
            val session = UserSessionEntity.findById(sessionId)
            session?.let {
                it.accessToken = accessToken
                it.refreshToken = refreshToken
                it.userAgent = userAgent
                it.platform = platform
                it.expiresAt = LocalDateTime.ofInstant(expiresAt, java.time.ZoneId.systemDefault()) // Convertir a LocalDateTime
                it.updatedAt = LocalDateTime.now()
            }
        }
        fun findSessionById(userSessionEntityId: Int): UserSessionEntity? {
            return transaction {
                UserSessionEntity.findById(userSessionEntityId)
            }
        }
        fun deleteSessionById(sessionId: Int) {
            return transaction {
                UserSessionEntity.findById(sessionId)?.delete()
            }
        }
    }
}