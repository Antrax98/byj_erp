package dev.byjtech.erp.machinery.domain.repository

import dev.byjtech.erp.machinery.domain.model.MachineryHistory
import java.util.UUID

interface MachineryHistoryRepository {
    fun save(history: MachineryHistory): MachineryHistory
    fun findById(id: UUID): MachineryHistory?
    fun findByMachineryId(machineryId: UUID): List<MachineryHistory>
    fun findByUserId(userId: UUID): List<MachineryHistory>
    fun findByField(field: String): List<MachineryHistory>
    fun findByMachineryIdAndField(machineryId: UUID, field: String): List<MachineryHistory>
    fun findAll(): List<MachineryHistory>
    fun delete(id: UUID)
}
