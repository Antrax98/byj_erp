package dev.byjtech.erp.machinery.infrastructure.exposed.repository

import dev.byjtech.erp.machinery.domain.model.MaintenanceSchedule
import dev.byjtech.erp.machinery.domain.repository.MaintenanceScheduleRepository
import dev.byjtech.erp.machinery.infrastructure.exposed.entities.MachineryEntity
import dev.byjtech.erp.machinery.infrastructure.exposed.entities.MaintenanceScheduleEntity
import dev.byjtech.erp.machinery.infrastructure.exposed.extensions.fromModel
import dev.byjtech.erp.machinery.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.machinery.infrastructure.exposed.tables.MaintenanceSchedulesTable
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class MaintenanceScheduleRepositoryImpl(private val db: Database) : MaintenanceScheduleRepository {
    
    override fun save(schedule: MaintenanceSchedule): MaintenanceSchedule {
        return transaction(db) {
            val machinery = MachineryEntity.findById(schedule.machineryId)
                ?: throw IllegalArgumentException("Machinery with id ${schedule.machineryId} not found")
            
            val entity = MaintenanceScheduleEntity.new {
                this.machinery = machinery
                name = schedule.name
                description = schedule.description
                intervalType = schedule.intervalType
                intervalValue = schedule.intervalValue
                lastMaintenanceDate = schedule.lastMaintenanceDate?.toJavaLocalDateTime()
                nextMaintenanceDate = schedule.nextMaintenanceDate?.toJavaLocalDateTime()
                lastOperationalValue = schedule.lastOperationalValue
                nextOperationalValue = schedule.nextOperationalValue
                createdBy = schedule.createdBy
                createdAt = java.time.LocalDateTime.now()
                updatedAt = java.time.LocalDateTime.now()
                isActive = schedule.isActive
            }
            entity.toModel()
        }
    }

    override fun findById(id: UUID): MaintenanceSchedule? {
        return transaction(db) {
            MaintenanceScheduleEntity.findById(id)?.toModel()
        }
    }

    override fun findByMachineryId(machineryId: UUID): List<MaintenanceSchedule> {
        return transaction(db) {
            MaintenanceScheduleEntity.find { MaintenanceSchedulesTable.machineryId eq machineryId }
                .map { it.toModel() }
        }
    }

    override fun findActiveByMachineryId(machineryId: UUID): List<MaintenanceSchedule> {
        return transaction(db) {
            MaintenanceScheduleEntity.find { 
                (MaintenanceSchedulesTable.machineryId eq machineryId) and 
                (MaintenanceSchedulesTable.isActive eq true)
            }.map { it.toModel() }
        }
    }

    override fun findAll(): List<MaintenanceSchedule> {
        return transaction(db) {
            MaintenanceScheduleEntity.all().map { it.toModel() }
        }
    }

    override fun update(schedule: MaintenanceSchedule): MaintenanceSchedule {
        return transaction(db) {
            val entity = MaintenanceScheduleEntity.findById(schedule.id)
                ?: throw IllegalArgumentException("MaintenanceSchedule with id ${schedule.id} not found")
            entity.fromModel(schedule)
            entity.updatedAt = java.time.LocalDateTime.now()
            entity.toModel()
        }
    }

    override fun delete(id: UUID) {
        transaction(db) {
            MaintenanceScheduleEntity.findById(id)?.delete()
        }
    }

    override fun findByCreatedBy(createdBy: UUID): List<MaintenanceSchedule> {
        return transaction(db) {
            MaintenanceScheduleEntity.find { MaintenanceSchedulesTable.createdBy eq createdBy }
                .map { it.toModel() }
        }
    }

    override fun findDueSchedules(currentDate: LocalDateTime): List<MaintenanceSchedule> {
        return transaction(db) {
            MaintenanceScheduleEntity.find { 
                (MaintenanceSchedulesTable.nextMaintenanceDate lessEq currentDate.toJavaLocalDateTime()) and
                (MaintenanceSchedulesTable.isActive eq true)
            }.map { it.toModel() }
        }
    }

    override fun findOverdueSchedules(currentDate: LocalDateTime): List<MaintenanceSchedule> {
        return transaction(db) {
            MaintenanceScheduleEntity.find { 
                (MaintenanceSchedulesTable.nextMaintenanceDate less currentDate.toJavaLocalDateTime()) and
                (MaintenanceSchedulesTable.isActive eq true)
            }.map { it.toModel() }
        }
    }

    override fun findByIntervalType(intervalType: String): List<MaintenanceSchedule> {
        return transaction(db) {
            MaintenanceScheduleEntity.find { MaintenanceSchedulesTable.intervalType eq intervalType }
                .map { it.toModel() }
        }
    }
}
