package dev.byjtech.erp.core.database.userSessions

import dev.byjtech.erp.config.logger
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.SessionEntity
import dev.byjtech.erp.core.infrastructure.exposed.tables.SessionsTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.Instant

//TODO: ELIMINAR y reemplazar con un service de sesion (o el mismo sessionRepositoryImpl serviria?)
class UserSessionsDataSource {
    companion object {
        fun findUserIdBySessionId(sessionId: Int): Int? = transaction {
            val session = SessionEntity.findById(sessionId)
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
                SessionEntity.find {
                    (SessionsTable.userId eq userId) and
                            (SessionsTable.deviceId eq deviceId) and
                            (SessionsTable.isValid eq true)
                }.forEach {
                    it.isValid = false
                }

                val newSession = SessionEntity.new {
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
        fun createSession(userId: Int, accessToken: String, refreshToken: String, userAgent: String, deviceId: String, platform: String, expiresAt: Instant): SessionEntity { // Añadido
            return transaction {
                SessionEntity.new {
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
        fun findSessionByUserIdAndDeviceId(userId: Int, deviceId: String): SessionEntity? = transaction {
            addLogger(StdOutSqlLogger) // Enable SQL logging for this transaction
            logger.debug("Finding session for userId: $userId, deviceId: $deviceId")
            val query = SessionEntity.find {
                (SessionsTable.userId eq userId) and
                        (SessionsTable.deviceId eq deviceId) and
                        (SessionsTable.isValid eq true)
            }
                .orderBy(SessionsTable.updatedAt to SortOrder.DESC)
                .limit(1)
            val session = query.firstOrNull()
            logger.debug("Found session: $session")
            session
        }

        fun updateSession(sessionId: Int, accessToken: String, refreshToken: String, userAgent: String, platform: String, expiresAt: Instant) = transaction { // Añadido
            val session = SessionEntity.findById(sessionId)
            session?.let {
                it.accessToken = accessToken
                it.refreshToken = refreshToken
                it.userAgent = userAgent
                it.platform = platform
                it.expiresAt = LocalDateTime.ofInstant(expiresAt, java.time.ZoneId.systemDefault()) // Convertir a LocalDateTime
                it.updatedAt = LocalDateTime.now()
            }
        }
        fun findSessionById(userSessionEntityId: Int): SessionEntity? {
            return transaction {
                SessionEntity.findById(userSessionEntityId)
            }
        }
        fun deleteSessionById(sessionId: Int) {
            return transaction {
                SessionEntity.findById(sessionId)?.delete()
            }
        }
    }
}