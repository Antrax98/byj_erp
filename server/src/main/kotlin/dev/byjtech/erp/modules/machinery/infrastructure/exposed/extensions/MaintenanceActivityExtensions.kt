package dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions

import dev.byjtech.erp.modules.machinery.domain.model.MaintenanceActivity
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MaintenanceActivityEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toJavaLocalDateTime

fun MaintenanceActivityEntity.toModel(): MaintenanceActivity {
    return MaintenanceActivity(
        id = this.id.value,
        scheduleId = this.schedule.id.value,
        name = this.name,
        description = this.description,
        estimatedDuration = this.estimatedDuration,
        createdAt = this.createdAt.toKotlinx(),
        updatedAt = this.updatedAt.toKotlinx(),
        isActive = this.isActive
    )
}

fun MaintenanceActivityEntity.fromModel(activity: MaintenanceActivity) {
    this.name = activity.name
    this.description = activity.description
    this.estimatedDuration = activity.estimatedDuration
    this.createdAt = activity.createdAt.toJavaLocalDateTime()
    this.updatedAt = activity.updatedAt.toJavaLocalDateTime()
    this.isActive = activity.isActive
}
