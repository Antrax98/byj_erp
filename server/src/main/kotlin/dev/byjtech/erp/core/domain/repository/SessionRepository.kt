package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.core.domain.model.Session

interface SessionRepository {
    fun create(session: Session): Session
    fun update(session: Session): Session
    fun find(id: Int): Session?
    fun findByUserId(userId: Int): Set<Session>
    fun findByDeviceId(deviceId: String): Session?
    fun delete(id: Int)
}