package dev.byjtech.erp.core.database.modules

import dev.byjtech.erp.core.dto.module.ModuleDTO
import dev.byjtech.erp.utils.datetime.toKotlinx

fun dev.byjtech.erp.core.database.modules.ModuleEntity.toDTO(): dev.byjtech.erp.core.dto.module.ModuleDTO {
    return dev.byjtech.erp.core.dto.module.ModuleDTO(
        id = this.id.value,
        name = this.name,
        displayName = this.displayName,
        description = this.description,
        version = this.version,
        createdAt = this.createdAt.toKotlinx(),
        updatedAt = this.updatedAt.toKotlinx(),
        developerOnly = this.developerOnly
    )
}