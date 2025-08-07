package dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions

import dev.byjtech.erp.modules.machinery.domain.model.MaintenanceSchedule
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MaintenanceScheduleEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toJavaLocalDateTime

fun MaintenanceScheduleEntity.toModel(): MaintenanceSchedule {
    return MaintenanceSchedule(
        id = this.id.value,
        machineryId = this.machinery.id.value,
        name = this.name,
        description = this.description,
        intervalType = this.intervalType,
        intervalValue = this.intervalValue,
        lastMaintenanceDate = this.lastMaintenanceDate?.toKotlinx(),
        nextMaintenanceDate = this.nextMaintenanceDate?.toKotlinx(),
        lastOperationalValue = this.lastOperationalValue,
        nextOperationalValue = this.nextOperationalValue,
        createdBy = this.createdBy,
        createdAt = this.createdAt.toKotlinx(),
        updatedAt = this.updatedAt.toKotlinx(),
        isActive = this.isActive
    )
}

fun MaintenanceScheduleEntity.fromModel(schedule: MaintenanceSchedule) {
    this.name = schedule.name
    this.description = schedule.description
    this.intervalType = schedule.intervalType
    this.intervalValue = schedule.intervalValue
    this.lastMaintenanceDate = schedule.lastMaintenanceDate?.toJavaLocalDateTime()
    this.nextMaintenanceDate = schedule.nextMaintenanceDate?.toJavaLocalDateTime()
    this.lastOperationalValue = schedule.lastOperationalValue
    this.nextOperationalValue = schedule.nextOperationalValue
    this.createdBy = schedule.createdBy
    this.createdAt = schedule.createdAt.toJavaLocalDateTime()
    this.updatedAt = schedule.updatedAt.toJavaLocalDateTime()
    this.isActive = schedule.isActive
}
