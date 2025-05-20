package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.dto.ModuleDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.ModuleEntity
import dev.byjtech.erp.utils.datetime.toKotlinx

fun ModuleEntity.toDTO(): ModuleDTO {
    return ModuleDTO(
        id = this.id.value,
        name = this.name,
        displayName = this.displayName,
        description = this.description,
        createdAt = this.createdAt?.toKotlinx(),
        updatedAt = this.updatedAt?.toKotlinx(),
        developerOnly = this.developerOnly,
    )
}