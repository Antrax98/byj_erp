package dev.byjtech.erp.machinery.domain.repository

import dev.byjtech.erp.machinery.domain.model.WorkOrderActivity
import java.util.UUID

interface WorkOrderActivityRepository {
    fun save(activity: WorkOrderActivity): WorkOrderActivity
    fun findById(id: UUID): WorkOrderActivity?
    fun findByWorkOrderId(workOrderId: UUID): List<WorkOrderActivity>
    fun findByActivityId(activityId: Int): List<WorkOrderActivity>
    fun findByStatus(status: String): List<WorkOrderActivity>
    fun findAll(): List<WorkOrderActivity>
    fun update(activity: WorkOrderActivity): WorkOrderActivity
    fun delete(id: UUID)
    fun findPendingByWorkOrderId(workOrderId: UUID): List<WorkOrderActivity>
    fun findCompletedByWorkOrderId(workOrderId: UUID): List<WorkOrderActivity>
}
