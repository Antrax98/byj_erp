package dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions

import dev.byjtech.erp.modules.machinery.domain.model.MachineryNotification
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MachineryNotificationEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toJavaLocalDateTime

fun MachineryNotificationEntity.toModel(): MachineryNotification {
    return MachineryNotification(
        id = this.id.value,
        machineryId = this.machinery?.id?.value,
        documentId = this.document?.id?.value,
        scheduleId = this.schedule?.id?.value,
        type = this.type,
        title = this.title,
        message = this.message,
        priority = this.priority,
        status = this.status,
        dueDate = this.dueDate?.toKotlinx(),
        notifyUsers = this.notifyUsers,
        createdAt = this.createdAt.toKotlinx(),
        updatedAt = this.updatedAt.toKotlinx()
    )
}

fun MachineryNotificationEntity.fromModel(notification: MachineryNotification) {
    this.type = notification.type
    this.title = notification.title
    this.message = notification.message
    this.priority = notification.priority
    this.status = notification.status
    this.dueDate = notification.dueDate?.toJavaLocalDateTime()
    this.notifyUsers = notification.notifyUsers
    this.createdAt = notification.createdAt.toJavaLocalDateTime()
    this.updatedAt = notification.updatedAt.toJavaLocalDateTime()
}
