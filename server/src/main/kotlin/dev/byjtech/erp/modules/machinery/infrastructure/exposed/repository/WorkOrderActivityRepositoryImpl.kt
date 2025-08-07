package dev.byjtech.erp.modules.machinery.infrastructure.exposed.repository

import dev.byjtech.erp.modules.machinery.domain.model.WorkOrderActivity
import dev.byjtech.erp.modules.machinery.domain.repository.WorkOrderActivityRepository
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MaintenanceActivityEntity
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.WorkOrderActivityEntity
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.WorkOrderEntity
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions.fromModel
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.WorkOrderActivitiesTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class WorkOrderActivityRepositoryImpl(private val db: Database) : WorkOrderActivityRepository {
    
    override fun save(activity: WorkOrderActivity): WorkOrderActivity {
        return transaction(db) {
            val workOrder = WorkOrderEntity.findById(activity.workOrderId)
                ?: throw IllegalArgumentException("WorkOrder with id ${activity.workOrderId} not found")
            
            val maintenanceActivity = activity.activityId?.let { 
                MaintenanceActivityEntity.findById(it)
                    ?: throw IllegalArgumentException("MaintenanceActivity with id $it not found")
            }
            
            val entity = WorkOrderActivityEntity.new {
                this.workOrder = workOrder
                this.activity = maintenanceActivity
                name = activity.name
                description = activity.description
                status = activity.status
                startDate = activity.startDate?.let { 
                    java.time.LocalDateTime.of(it.year, it.monthNumber, it.dayOfMonth, it.hour, it.minute, it.second)
                }
                completionDate = activity.completionDate?.let { 
                    java.time.LocalDateTime.of(it.year, it.monthNumber, it.dayOfMonth, it.hour, it.minute, it.second)
                }
                comments = activity.comments
                createdAt = java.time.LocalDateTime.now()
                updatedAt = java.time.LocalDateTime.now()
            }
            entity.toModel()
        }
    }

    override fun findById(id: UUID): WorkOrderActivity? {
        return transaction(db) {
            WorkOrderActivityEntity.findById(id)?.toModel()
        }
    }

    override fun findByWorkOrderId(workOrderId: UUID): List<WorkOrderActivity> {
        return transaction(db) {
            WorkOrderActivityEntity.find { WorkOrderActivitiesTable.workOrderId eq workOrderId }
                .map { it.toModel() }
        }
    }

    override fun findByActivityId(activityId: Int): List<WorkOrderActivity> {
        return transaction(db) {
            WorkOrderActivityEntity.find { WorkOrderActivitiesTable.activityId eq activityId }
                .map { it.toModel() }
        }
    }

    override fun findByStatus(status: String): List<WorkOrderActivity> {
        return transaction(db) {
            WorkOrderActivityEntity.find { WorkOrderActivitiesTable.status eq status }
                .map { it.toModel() }
        }
    }

    override fun findAll(): List<WorkOrderActivity> {
        return transaction(db) {
            WorkOrderActivityEntity.all().map { it.toModel() }
        }
    }

    override fun update(activity: WorkOrderActivity): WorkOrderActivity {
        return transaction(db) {
            val entity = WorkOrderActivityEntity.findById(activity.id)
                ?: throw IllegalArgumentException("WorkOrderActivity with id ${activity.id} not found")
            entity.fromModel(activity)
            entity.updatedAt = java.time.LocalDateTime.now()
            entity.toModel()
        }
    }

    override fun delete(id: UUID) {
        transaction(db) {
            WorkOrderActivityEntity.findById(id)?.delete()
        }
    }

    override fun findPendingByWorkOrderId(workOrderId: UUID): List<WorkOrderActivity> {
        return transaction(db) {
            WorkOrderActivityEntity.find { 
                (WorkOrderActivitiesTable.workOrderId eq workOrderId) and 
                (WorkOrderActivitiesTable.status eq "PENDING")
            }.map { it.toModel() }
        }
    }

    override fun findCompletedByWorkOrderId(workOrderId: UUID): List<WorkOrderActivity> {
        return transaction(db) {
            WorkOrderActivityEntity.find { 
                (WorkOrderActivitiesTable.workOrderId eq workOrderId) and 
                (WorkOrderActivitiesTable.status eq "COMPLETED")
            }.map { it.toModel() }
        }
    }
}
