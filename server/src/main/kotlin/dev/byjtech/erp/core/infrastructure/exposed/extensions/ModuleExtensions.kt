package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.dto.ModuleDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.ModuleEntity
import dev.byjtech.erp.core.domain.model.Module
import dev.byjtech.erp.core.infrastructure.exposed.entities.CategoryEntity
import dev.byjtech.erp.utils.datetime.toKotlinx

fun ModuleEntity.toDTO(): ModuleDTO {
    return ModuleDTO(
        id = this.id.value,
        name = this.name,
        displayName = this.displayName,
        description = this.description,
        developerOnly = this.developerOnly,
    )
}

fun ModuleEntity.toModel(categoriesSet: Set<CategoryEntity>? = null): Module {
    val categories = categoriesSet?.map { it.toModel() }?.toSet()
    return Module(
        id = this.id.value,
        name = this.name,
        displayName = this.displayName,
        description = this.description,
        developerOnly = this.developerOnly,
        categories = categories
    )
}

fun Module.toDTO(): ModuleDTO {
    return ModuleDTO(
        id = this.id,
        name = this.name,
        displayName = this.displayName,
        description = this.description,
        developerOnly = this.developerOnly,
    )
}