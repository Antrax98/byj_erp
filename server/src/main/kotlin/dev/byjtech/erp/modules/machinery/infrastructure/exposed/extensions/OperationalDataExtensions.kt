package dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions

import dev.byjtech.erp.modules.machinery.domain.model.OperationalData
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.OperationalDataEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toJavaLocalDateTime

fun OperationalDataEntity.toModel(): OperationalData {
    return OperationalData(
        id = this.id.value,
        machineryId = this.machinery.id.value,
        type = this.type,
        value = this.value,
        recordedAt = this.recordedAt.toKotlinx(),
        recordedBy = this.recordedBy,
        createdAt = this.createdAt.toKotlinx(),
        updatedAt = this.updatedAt.toKotlinx()
    )
}

fun OperationalDataEntity.fromModel(operationalData: OperationalData) {
    this.type = operationalData.type
    this.value = operationalData.value
    this.recordedAt = operationalData.recordedAt.toJavaLocalDateTime()
    this.recordedBy = operationalData.recordedBy
    this.createdAt = operationalData.createdAt.toJavaLocalDateTime()
    this.updatedAt = operationalData.updatedAt.toJavaLocalDateTime()
}
