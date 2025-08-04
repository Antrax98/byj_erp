package dev.byjtech.erp.machinery.infrastructure.exposed.extensions

import dev.byjtech.erp.machinery.domain.model.MachineryHistory
import dev.byjtech.erp.machinery.infrastructure.exposed.entities.MachineryHistoryEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toJavaLocalDateTime

fun MachineryHistoryEntity.toModel(): MachineryHistory {
    return MachineryHistory(
        id = this.id.value,
        machineryId = this.machinery.id.value,
        userId = this.userId,
        field = this.field,
        oldValue = this.oldValue,
        newValue = this.newValue,
        createdAt = this.createdAt.toKotlinx(),
        comment = this.comment
    )
}

fun MachineryHistoryEntity.fromModel(history: MachineryHistory) {
    this.userId = history.userId
    this.field = history.field
    this.oldValue = history.oldValue
    this.newValue = history.newValue
    this.createdAt = history.createdAt.toJavaLocalDateTime()
    this.comment = history.comment
}
