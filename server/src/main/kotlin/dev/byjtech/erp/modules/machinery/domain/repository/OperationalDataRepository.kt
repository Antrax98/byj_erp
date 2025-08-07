package dev.byjtech.erp.modules.machinery.domain.repository

import dev.byjtech.erp.modules.machinery.domain.model.OperationalData
import java.util.UUID

interface OperationalDataRepository {
    fun save(operationalData: OperationalData): OperationalData
    fun findById(id: UUID): OperationalData?
    fun findByMachineryId(machineryId: UUID): List<OperationalData>
    fun findByMachineryIdAndType(machineryId: UUID, type: String): List<OperationalData>
    fun findLatestByMachineryId(machineryId: UUID): OperationalData?
    fun findLatestByMachineryIdAndType(machineryId: UUID, type: String): OperationalData?
    fun update(operationalData: OperationalData): OperationalData
    fun delete(id: UUID)
    fun findByRecordedBy(recordedBy: UUID): List<OperationalData>
}
