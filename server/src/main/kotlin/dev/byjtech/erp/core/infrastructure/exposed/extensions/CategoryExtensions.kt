package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.domain.model.Category
import dev.byjtech.erp.core.dto.CategoryDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.CategoryEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.PermissionEntity
import dev.byjtech.erp.utils.datetime.toKotlinx

// Extensión para convertir CategoryEntity a CategoryDTO
fun CategoryEntity.toDTO(): CategoryDTO {
    return CategoryDTO(
        id = this.id.value.toString(),
        name = this.name,
        description = this.description,
        moduleId = this.module.id.value.toString() // Si 'module' es nullable, maneja el valor como nullable también
    )
}

fun CategoryEntity.toModel(permissionsSet: Set<PermissionEntity>? = null): Category {
    val permissions = permissionsSet?.map { it.toModel() }?.toSet()
    return Category(
        id = this.id.value,
        name = this.name,
        description = this.description,
        permissions = permissions
    )
}