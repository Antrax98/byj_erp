package dev.byjtech.erp.machinery.infrastructure.exposed.repository

import dev.byjtech.erp.machinery.domain.model.OperationalData
import dev.byjtech.erp.machinery.domain.repository.OperationalDataRepository
import dev.byjtech.erp.machinery.infrastructure.exposed.entities.MachineryEntity
import dev.byjtech.erp.machinery.infrastructure.exposed.entities.OperationalDataEntity
import dev.byjtech.erp.machinery.infrastructure.exposed.extensions.fromModel
import dev.byjtech.erp.machinery.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.machinery.infrastructure.exposed.tables.OperationalDataTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.UUID

class OperationalDataRepositoryImpl(private val db: Database) : OperationalDataRepository {
    
    override fun save(operationalData: OperationalData): OperationalData {
        return transaction(db) {
            val machinery = MachineryEntity.findById(operationalData.machineryId)
                ?: throw IllegalArgumentException("Machinery with id ${operationalData.machineryId} not found")
            
            val entity = OperationalDataEntity.new {
                this.machinery = machinery
                type = operationalData.type
                value = operationalData.value
                recordedAt = operationalData.recordedAt.let { 
                    java.time.LocalDateTime.of(it.year, it.monthNumber, it.dayOfMonth, it.hour, it.minute, it.second)
                }
                recordedBy = operationalData.recordedBy
                createdAt = LocalDateTime.now()
                updatedAt = LocalDateTime.now()
            }
            entity.toModel()
        }
    }

    override fun findById(id: UUID): OperationalData? {
        return transaction(db) {
            OperationalDataEntity.findById(id)?.toModel()
        }
    }

    override fun findByMachineryId(machineryId: UUID): List<OperationalData> {
        return transaction(db) {
            OperationalDataEntity.find { OperationalDataTable.machineryId eq machineryId }
                .orderBy(OperationalDataTable.recordedAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun findByMachineryIdAndType(machineryId: UUID, type: String): List<OperationalData> {
        return transaction(db) {
            OperationalDataEntity.find { 
                (OperationalDataTable.machineryId eq machineryId) and (OperationalDataTable.type eq type)
            }.orderBy(OperationalDataTable.recordedAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun findLatestByMachineryId(machineryId: UUID): OperationalData? {
        return transaction(db) {
            OperationalDataEntity.find { OperationalDataTable.machineryId eq machineryId }
                .orderBy(OperationalDataTable.recordedAt to SortOrder.DESC)
                .limit(1)
                .firstOrNull()?.toModel()
        }
    }

    override fun findLatestByMachineryIdAndType(machineryId: UUID, type: String): OperationalData? {
        return transaction(db) {
            OperationalDataEntity.find { 
                (OperationalDataTable.machineryId eq machineryId) and (OperationalDataTable.type eq type)
            }.orderBy(OperationalDataTable.recordedAt to SortOrder.DESC)
                .limit(1)
                .firstOrNull()?.toModel()
        }
    }

    override fun update(operationalData: OperationalData): OperationalData {
        return transaction(db) {
            val entity = OperationalDataEntity.findById(operationalData.id)
                ?: throw IllegalArgumentException("OperationalData with id ${operationalData.id} not found")
            entity.fromModel(operationalData)
            entity.updatedAt = LocalDateTime.now()
            entity.toModel()
        }
    }

    override fun delete(id: UUID) {
        transaction(db) {
            OperationalDataEntity.findById(id)?.delete()
        }
    }

    override fun findByRecordedBy(recordedBy: UUID): List<OperationalData> {
        return transaction(db) {
            OperationalDataEntity.find { OperationalDataTable.recordedBy eq recordedBy }
                .orderBy(OperationalDataTable.recordedAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }
}
