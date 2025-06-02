package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.core.domain.model.Session
import dev.byjtech.erp.core.domain.repository.SessionRepository
import dev.byjtech.erp.core.infrastructure.exposed.entities.SessionEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toModel
import org.jetbrains.exposed.sql.transactions.transaction

class SessionRepositoryImpl: SessionRepository {
    override fun create(session: Session): Session {
        TODO("Not yet implemented")
    }
    override fun update(session: Session): Session {
        TODO("Not yet implemented")
    }
    override fun find(id: Int): Session? {
        return transaction {
            val session = SessionEntity.findById(id)?.toModel()
            return@transaction session
        }
    }
    override fun findByUserId(userId: Int): Set<Session> {
        TODO("Not yet implemented")
    }
    override fun findByDeviceId(deviceId: String): Session? {
        TODO("Not yet implemented")
    }
    override fun delete(id: Int) {
        transaction {
            SessionEntity.findById(id)?.delete()
        }
    }

}