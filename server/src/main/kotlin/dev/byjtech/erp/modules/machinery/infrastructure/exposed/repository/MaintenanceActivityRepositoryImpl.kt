package dev.byjtech.erp.modules.machinery.infrastructure.exposed.repository

import dev.byjtech.erp.modules.machinery.domain.model.MaintenanceActivity
import dev.byjtech.erp.modules.machinery.domain.repository.MaintenanceActivityRepository
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MaintenanceActivityEntity
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MaintenanceScheduleEntity
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions.fromModel
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MaintenanceActivitiesTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class MaintenanceActivityRepositoryImpl(private val db: Database) : MaintenanceActivityRepository {
    
    override fun save(activity: MaintenanceActivity): MaintenanceActivity {
        return transaction(db) {
            val schedule = MaintenanceScheduleEntity.findById(activity.scheduleId)
                ?: throw IllegalArgumentException("MaintenanceSchedule with id ${activity.scheduleId} not found")
            
            val entity = MaintenanceActivityEntity.new {
                this.schedule = schedule
                name = activity.name
                description = activity.description
                estimatedDuration = activity.estimatedDuration
                createdAt = java.time.LocalDateTime.now()
                updatedAt = java.time.LocalDateTime.now()
                isActive = activity.isActive
            }
            entity.toModel()
        }
    }

    override fun findById(id: Int): MaintenanceActivity? {
        return transaction(db) {
            MaintenanceActivityEntity.findById(id)?.toModel()
        }
    }

    override fun findByScheduleId(scheduleId: UUID): List<MaintenanceActivity> {
        return transaction(db) {
            MaintenanceActivityEntity.find { MaintenanceActivitiesTable.scheduleId eq scheduleId }
                .map { it.toModel() }
        }
    }

    override fun findActiveByScheduleId(scheduleId: UUID): List<MaintenanceActivity> {
        return transaction(db) {
            MaintenanceActivityEntity.find { 
                (MaintenanceActivitiesTable.scheduleId eq scheduleId) and 
                (MaintenanceActivitiesTable.isActive eq true)
            }.map { it.toModel() }
        }
    }

    override fun findAll(): List<MaintenanceActivity> {
        return transaction(db) {
            MaintenanceActivityEntity.all().map { it.toModel() }
        }
    }

    override fun update(activity: MaintenanceActivity): MaintenanceActivity {
        return transaction(db) {
            val entity = MaintenanceActivityEntity.findById(activity.id ?: throw IllegalArgumentException("Activity id cannot be null"))
                ?: throw IllegalArgumentException("MaintenanceActivity with id ${activity.id} not found")
            entity.fromModel(activity)
            entity.updatedAt = java.time.LocalDateTime.now()
            entity.toModel()
        }
    }

    override fun delete(id: Int) {
        transaction(db) {
            MaintenanceActivityEntity.findById(id)?.delete()
        }
    }

    override fun findByName(name: String): List<MaintenanceActivity> {
        return transaction(db) {
            MaintenanceActivityEntity.find { MaintenanceActivitiesTable.name eq name }
                .map { it.toModel() }
        }
    }
}
