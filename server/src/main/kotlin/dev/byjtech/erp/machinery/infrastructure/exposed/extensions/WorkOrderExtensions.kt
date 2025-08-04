package dev.byjtech.erp.machinery.infrastructure.exposed.extensions

import dev.byjtech.erp.machinery.domain.model.WorkOrder
import dev.byjtech.erp.machinery.infrastructure.exposed.entities.WorkOrderEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toJavaLocalDateTime

fun WorkOrderEntity.toModel(): WorkOrder {
    return WorkOrder(
        id = this.id.value,
        machineryId = this.machinery.id.value,
        scheduleId = this.schedule?.id?.value,
        orderNumber = this.orderNumber,
        description = this.description,
        type = this.type,
        status = this.status,
        scheduledDate = this.scheduledDate.toKotlinx(),
        startDate = this.startDate?.toKotlinx(),
        completionDate = this.completionDate?.toKotlinx(),
        assignedTo = this.assignedTo,
        createdBy = this.createdBy,
        createdAt = this.createdAt.toKotlinx(),
        updatedAt = this.updatedAt.toKotlinx(),
        comments = this.comments
    )
}

fun WorkOrderEntity.fromModel(workOrder: WorkOrder) {
    this.orderNumber = workOrder.orderNumber
    this.description = workOrder.description
    this.type = workOrder.type
    this.status = workOrder.status
    this.scheduledDate = workOrder.scheduledDate.toJavaLocalDateTime()
    this.startDate = workOrder.startDate?.toJavaLocalDateTime()
    this.completionDate = workOrder.completionDate?.toJavaLocalDateTime()
    this.assignedTo = workOrder.assignedTo
    this.createdBy = workOrder.createdBy
    this.createdAt = workOrder.createdAt.toJavaLocalDateTime()
    this.updatedAt = workOrder.updatedAt.toJavaLocalDateTime()
    this.comments = workOrder.comments
}
