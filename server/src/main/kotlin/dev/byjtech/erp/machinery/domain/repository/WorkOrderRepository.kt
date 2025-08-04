package dev.byjtech.erp.machinery.domain.repository

import dev.byjtech.erp.machinery.domain.model.WorkOrder
import kotlinx.datetime.LocalDateTime
import java.util.UUID

interface WorkOrderRepository {
    fun save(workOrder: WorkOrder): WorkOrder
    fun findById(id: UUID): WorkOrder?
    fun findByOrderNumber(orderNumber: String): WorkOrder?
    fun findByMachineryId(machineryId: UUID): List<WorkOrder>
    fun findByScheduleId(scheduleId: UUID): List<WorkOrder>
    fun findByStatus(status: String): List<WorkOrder>
    fun findByAssignedTo(assignedTo: UUID): List<WorkOrder>
    fun findByCreatedBy(createdBy: UUID): List<WorkOrder>
    fun findAll(): List<WorkOrder>
    fun update(workOrder: WorkOrder): WorkOrder
    fun delete(id: UUID)
    fun findByType(type: String): List<WorkOrder>
    fun findByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<WorkOrder>
    fun findPendingWorkOrders(): List<WorkOrder>
    fun findOverdueWorkOrders(currentDate: LocalDateTime): List<WorkOrder>
    fun existsWithOrderNumber(orderNumber: String): Boolean
}
