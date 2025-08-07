package dev.byjtech.erp.modules.machinery.infrastructure.exposed.repository

import dev.byjtech.erp.modules.machinery.domain.model.MachineryHistory
import dev.byjtech.erp.modules.machinery.domain.repository.MachineryHistoryRepository
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MachineryEntity
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MachineryHistoryEntity
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions.fromModel
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryHistoriesTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class MachineryHistoryRepositoryImpl(private val db: Database) : MachineryHistoryRepository {
    
    override fun save(history: MachineryHistory): MachineryHistory {
        return transaction(db) {
            val machinery = MachineryEntity.findById(history.machineryId)
                ?: throw IllegalArgumentException("Machinery with id ${history.machineryId} not found")
            
            val entity = MachineryHistoryEntity.new {
                this.machinery = machinery
                userId = history.userId
                field = history.field
                oldValue = history.oldValue
                newValue = history.newValue
                createdAt = history.createdAt.let { 
                    java.time.LocalDateTime.of(it.year, it.monthNumber, it.dayOfMonth, it.hour, it.minute, it.second)
                }
                comment = history.comment
            }
            entity.toModel()
        }
    }

    override fun findById(id: UUID): MachineryHistory? {
        return transaction(db) {
            MachineryHistoryEntity.findById(id)?.toModel()
        }
    }

    override fun findByMachineryId(machineryId: UUID): List<MachineryHistory> {
        return transaction(db) {
            MachineryHistoryEntity.find { MachineryHistoriesTable.machineryId eq machineryId }
                .orderBy(MachineryHistoriesTable.createdAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun findByUserId(userId: UUID): List<MachineryHistory> {
        return transaction(db) {
            MachineryHistoryEntity.find { MachineryHistoriesTable.userId eq userId }
                .orderBy(MachineryHistoriesTable.createdAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun findByField(field: String): List<MachineryHistory> {
        return transaction(db) {
            MachineryHistoryEntity.find { MachineryHistoriesTable.field eq field }
                .orderBy(MachineryHistoriesTable.createdAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun findByMachineryIdAndField(machineryId: UUID, field: String): List<MachineryHistory> {
        return transaction(db) {
            MachineryHistoryEntity.find { 
                (MachineryHistoriesTable.machineryId eq machineryId) and 
                (MachineryHistoriesTable.field eq field)
            }.orderBy(MachineryHistoriesTable.createdAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun findAll(): List<MachineryHistory> {
        return transaction(db) {
            MachineryHistoryEntity.all()
                .orderBy(MachineryHistoriesTable.createdAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun delete(id: UUID) {
        transaction(db) {
            MachineryHistoryEntity.findById(id)?.delete()
        }
    }
}
