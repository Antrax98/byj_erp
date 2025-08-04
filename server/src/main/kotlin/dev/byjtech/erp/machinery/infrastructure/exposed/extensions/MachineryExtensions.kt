package dev.byjtech.erp.machinery.infrastructure.exposed.extensions

import dev.byjtech.erp.machinery.domain.model.Machinery
import dev.byjtech.erp.machinery.infrastructure.exposed.entities.MachineryEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toJavaLocalDateTime

fun MachineryEntity.toModel(): Machinery {
    return Machinery(
        id = this.id.value,
        code = this.code,
        name = this.name,
        description = this.description,
        brand = this.brand,
        model = this.model,
        year = this.year,
        serialNumber = this.serialNumber,
        licensePlate = this.licensePlate,
        status = this.status,
        location = this.location,
        companyId = this.companyId,
        createdAt = this.createdAt.toKotlinx(),
        updatedAt = this.updatedAt.toKotlinx(),
        isActive = this.isActive
    )
}

fun MachineryEntity.fromModel(machinery: Machinery) {
    this.code = machinery.code
    this.name = machinery.name
    this.description = machinery.description
    this.brand = machinery.brand
    this.model = machinery.model
    this.year = machinery.year
    this.serialNumber = machinery.serialNumber
    this.licensePlate = machinery.licensePlate
    this.status = machinery.status
    this.location = machinery.location
    this.companyId = machinery.companyId
    this.createdAt = machinery.createdAt.toJavaLocalDateTime()
    this.updatedAt = machinery.updatedAt.toJavaLocalDateTime()
    this.isActive = machinery.isActive
}
