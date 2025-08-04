package dev.byjtech.erp.machinery.infrastructure.exposed.repository

import dev.byjtech.erp.machinery.domain.model.WorkOrder
import dev.byjtech.erp.machinery.domain.repository.WorkOrderRepository
import dev.byjtech.erp.machinery.infrastructure.exposed.entities.MachineryEntity
import dev.byjtech.erp.machinery.infrastructure.exposed.entities.MaintenanceScheduleEntity
import dev.byjtech.erp.machinery.infrastructure.exposed.entities.WorkOrderEntity
import dev.byjtech.erp.machinery.infrastructure.exposed.extensions.fromModel
import dev.byjtech.erp.machinery.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.machinery.infrastructure.exposed.tables.WorkOrdersTable
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class WorkOrderRepositoryImpl(private val db: Database) : WorkOrderRepository {
    
    override fun save(workOrder: WorkOrder): WorkOrder {
        return transaction(db) {
            val machinery = MachineryEntity.findById(workOrder.machineryId)
                ?: throw IllegalArgumentException("Machinery with id ${workOrder.machineryId} not found")
            
            val schedule = workOrder.scheduleId?.let { 
                MaintenanceScheduleEntity.findById(it)
                    ?: throw IllegalArgumentException("MaintenanceSchedule with id $it not found")
            }
            
            val entity = WorkOrderEntity.new {
                this.machinery = machinery
                this.schedule = schedule
                orderNumber = workOrder.orderNumber
                description = workOrder.description
                type = workOrder.type
                status = workOrder.status
                scheduledDate = workOrder.scheduledDate.toJavaLocalDateTime()
                startDate = workOrder.startDate?.toJavaLocalDateTime()
                completionDate = workOrder.completionDate?.toJavaLocalDateTime()
                assignedTo = workOrder.assignedTo
                createdBy = workOrder.createdBy
                createdAt = java.time.LocalDateTime.now()
                updatedAt = java.time.LocalDateTime.now()
                comments = workOrder.comments
            }
            entity.toModel()
        }
    }

    override fun findById(id: UUID): WorkOrder? {
        return transaction(db) {
            WorkOrderEntity.findById(id)?.toModel()
        }
    }

    override fun findByOrderNumber(orderNumber: String): WorkOrder? {
        return transaction(db) {
            WorkOrderEntity.find { WorkOrdersTable.orderNumber eq orderNumber }.firstOrNull()?.toModel()
        }
    }

    override fun findByMachineryId(machineryId: UUID): List<WorkOrder> {
        return transaction(db) {
            WorkOrderEntity.find { WorkOrdersTable.machineryId eq machineryId }.map { it.toModel() }
        }
    }

    override fun findByScheduleId(scheduleId: UUID): List<WorkOrder> {
        return transaction(db) {
            WorkOrderEntity.find { WorkOrdersTable.scheduleId eq scheduleId }.map { it.toModel() }
        }
    }

    override fun findByStatus(status: String): List<WorkOrder> {
        return transaction(db) {
            WorkOrderEntity.find { WorkOrdersTable.status eq status }.map { it.toModel() }
        }
    }

    override fun findByAssignedTo(assignedTo: UUID): List<WorkOrder> {
        return transaction(db) {
            WorkOrderEntity.find { WorkOrdersTable.assignedTo eq assignedTo }.map { it.toModel() }
        }
    }

    override fun findByCreatedBy(createdBy: UUID): List<WorkOrder> {
        return transaction(db) {
            WorkOrderEntity.find { WorkOrdersTable.createdBy eq createdBy }.map { it.toModel() }
        }
    }

    override fun findAll(): List<WorkOrder> {
        return transaction(db) {
            WorkOrderEntity.all().map { it.toModel() }
        }
    }

    override fun update(workOrder: WorkOrder): WorkOrder {
        return transaction(db) {
            val entity = WorkOrderEntity.findById(workOrder.id)
                ?: throw IllegalArgumentException("WorkOrder with id ${workOrder.id} not found")
            entity.fromModel(workOrder)
            entity.updatedAt = java.time.LocalDateTime.now()
            entity.toModel()
        }
    }

    override fun delete(id: UUID) {
        transaction(db) {
            WorkOrderEntity.findById(id)?.delete()
        }
    }

    override fun findByType(type: String): List<WorkOrder> {
        return transaction(db) {
            WorkOrderEntity.find { WorkOrdersTable.type eq type }.map { it.toModel() }
        }
    }

    override fun findByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<WorkOrder> {
        return transaction(db) {
            WorkOrderEntity.find { 
                (WorkOrdersTable.scheduledDate greaterEq startDate.toJavaLocalDateTime()) and
                (WorkOrdersTable.scheduledDate lessEq endDate.toJavaLocalDateTime())
            }.map { it.toModel() }
        }
    }

    override fun findPendingWorkOrders(): List<WorkOrder> {
        return transaction(db) {
            WorkOrderEntity.find { WorkOrdersTable.status eq "PENDING" }.map { it.toModel() }
        }
    }

    override fun findOverdueWorkOrders(currentDate: LocalDateTime): List<WorkOrder> {
        return transaction(db) {
            WorkOrderEntity.find { 
                (WorkOrdersTable.scheduledDate less currentDate.toJavaLocalDateTime()) and
                (WorkOrdersTable.status neq "COMPLETED")
            }.map { it.toModel() }
        }
    }

    override fun existsWithOrderNumber(orderNumber: String): Boolean {
        return transaction(db) {
            !WorkOrderEntity.find { WorkOrdersTable.orderNumber eq orderNumber }.empty()
        }
    }
}
