package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.core.domain.model.Session
import java.util.UUID

interface SessionRepository {
    fun create(session: Session): Session
    fun update(session: Session): Session
    fun find(id: UUID): Session?
    fun findByUserId(userId: UUID): Set<Session>
    fun findByDeviceId(deviceId: String): Session?
    fun delete(id: UUID)
}