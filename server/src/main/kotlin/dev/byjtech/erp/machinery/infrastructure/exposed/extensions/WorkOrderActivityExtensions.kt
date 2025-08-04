package dev.byjtech.erp.machinery.infrastructure.exposed.extensions

import dev.byjtech.erp.machinery.domain.model.WorkOrderActivity
import dev.byjtech.erp.machinery.infrastructure.exposed.entities.WorkOrderActivityEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toJavaLocalDateTime

fun WorkOrderActivityEntity.toModel(): WorkOrderActivity {
    return WorkOrderActivity(
        id = this.id.value,
        workOrderId = this.workOrder.id.value,
        activityId = this.activity?.id?.value,
        name = this.name,
        description = this.description,
        status = this.status,
        startDate = this.startDate?.toKotlinx(),
        completionDate = this.completionDate?.toKotlinx(),
        comments = this.comments,
        createdAt = this.createdAt.toKotlinx(),
        updatedAt = this.updatedAt.toKotlinx()
    )
}

fun WorkOrderActivityEntity.fromModel(activity: WorkOrderActivity) {
    this.name = activity.name
    this.description = activity.description
    this.status = activity.status
    this.startDate = activity.startDate?.toJavaLocalDateTime()
    this.completionDate = activity.completionDate?.toJavaLocalDateTime()
    this.comments = activity.comments
    this.createdAt = activity.createdAt.toJavaLocalDateTime()
    this.updatedAt = activity.updatedAt.toJavaLocalDateTime()
}
