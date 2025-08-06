package dev.byjtech.erp.machinery.infrastructure.repository

import dev.byjtech.erp.machinery.domain.model.MachineryHistory
import dev.byjtech.erp.machinery.domain.repository.MachineryHistoryRepository
import dev.byjtech.erp.machinery.infrastructure.exposed.entities.MachineryEntity
import dev.byjtech.erp.machinery.infrastructure.exposed.entities.MachineryHistoryEntity
import dev.byjtech.erp.machinery.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.machinery.infrastructure.exposed.tables.MachineryHistoriesTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class MachineryHistoryRepositoryImpl : MachineryHistoryRepository {
    
    override fun save(history: MachineryHistory): MachineryHistory = transaction {
        val machinery = MachineryEntity.findById(history.machineryId)
            ?: throw IllegalArgumentException("Machinery with id ${history.machineryId} not found")
            
        val entity = MachineryHistoryEntity.new {
            this.machinery = machinery
            this.userId = history.userId
            this.field = history.field
            this.oldValue = history.oldValue
            this.newValue = history.newValue
            this.createdAt = java.time.LocalDateTime.of(
                history.createdAt.year,
                history.createdAt.month,
                history.createdAt.dayOfMonth,
                history.createdAt.hour,
                history.createdAt.minute,
                history.createdAt.second,
                history.createdAt.nanosecond
            )
            this.comment = history.comment
        }
        
        entity.toModel()
    }
    
    override fun findById(id: UUID): MachineryHistory? = transaction {
        MachineryHistoryEntity.findById(id)?.toModel()
    }
    
    override fun findByMachineryId(machineryId: UUID): List<MachineryHistory> = transaction {
        MachineryHistoryEntity.find { MachineryHistoriesTable.machineryId eq machineryId }
            .orderBy(MachineryHistoriesTable.createdAt to SortOrder.DESC)
            .map { it.toModel() }
    }
    
    override fun findByUserId(userId: UUID): List<MachineryHistory> = transaction {
        MachineryHistoryEntity.find { MachineryHistoriesTable.userId eq userId }
            .orderBy(MachineryHistoriesTable.createdAt to SortOrder.DESC)
            .map { it.toModel() }
    }
    
    override fun findByField(field: String): List<MachineryHistory> = transaction {
        MachineryHistoryEntity.find { MachineryHistoriesTable.field eq field }
            .orderBy(MachineryHistoriesTable.createdAt to SortOrder.DESC)
            .map { it.toModel() }
    }
    
    override fun findByMachineryIdAndField(machineryId: UUID, field: String): List<MachineryHistory> = transaction {
        MachineryHistoryEntity.find { 
            (MachineryHistoriesTable.machineryId eq machineryId) and (MachineryHistoriesTable.field eq field)
        }
        .orderBy(MachineryHistoriesTable.createdAt to SortOrder.DESC)
        .map { it.toModel() }
    }
    
    override fun findAll(): List<MachineryHistory> = transaction {
        MachineryHistoryEntity.all()
            .orderBy(MachineryHistoriesTable.createdAt to SortOrder.DESC)
            .map { it.toModel() }
    }
    
    override fun delete(id: UUID): Unit = transaction {
        MachineryHistoryEntity.findById(id)?.delete()
        Unit
    }
}
