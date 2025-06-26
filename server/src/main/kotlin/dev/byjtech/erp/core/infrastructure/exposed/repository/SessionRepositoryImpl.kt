package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.core.domain.model.Session
import dev.byjtech.erp.core.domain.repository.SessionRepository
import dev.byjtech.erp.core.infrastructure.exposed.entities.SessionEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.core.infrastructure.exposed.tables.SessionsTable
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.UUID

class SessionRepositoryImpl(private val db: Database): SessionRepository {
    override fun create(session: Session): Session {
        return transaction(db) {
            val sessionEntity = SessionEntity.new(UUID.randomUUID()) {
                this.user = UserEntity[session.userId]
                this.deviceId = session.deviceId
                this.platform = session.platform
                this.userAgent = session.userAgent
                this.isValid = session.isValid
                this.accessToken = session.accessTokens
                this.refreshToken = session.refreshToken
                this.createdAt = LocalDateTime.now()
                this.updatedAt = LocalDateTime.now()
                this.expiresAt = session.expiresAt.toJavaLocalDateTime()
            }
            return@transaction sessionEntity.toModel()
        }
    }
    override fun update(session: Session): Session {
        return transaction(db) {
            val sessionEntity = SessionEntity.findById(session.id)
            sessionEntity?.let {
                it.deviceId = session.deviceId
                it.updatedAt = LocalDateTime.now()
                it.platform = session.platform
                it.userAgent = session.userAgent
                it.isValid = session.isValid
                it.accessToken = session.accessTokens
                it.refreshToken = session.refreshToken
                it.expiresAt = session.expiresAt.toJavaLocalDateTime()
            }
            return@transaction sessionEntity?.toModel() ?: throw Exception("Session not found")
        }
    }
    override fun find(id: UUID): Session? {
        return transaction(db) {
            val session = SessionEntity.findById(id)?.toModel()
            return@transaction session
        }
    }
    override fun findByUserId(userId: UUID): Set<Session> {
        return transaction(db) {
            val sessions = SessionEntity.find { SessionsTable.userId eq userId }.map { it.toModel() }
            return@transaction sessions.toSet()
        }
    }
    override fun findByDeviceId(deviceId: String): Session? {
        TODO("Not yet implemented")
    }
    override fun delete(id: UUID) {
        transaction(db) {
            SessionEntity.findById(id)?.delete()
        }
    }

}