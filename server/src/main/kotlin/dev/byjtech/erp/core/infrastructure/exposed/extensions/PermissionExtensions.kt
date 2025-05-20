package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.dto.PermissionDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.PermissionEntity

fun PermissionEntity.toDTO(): PermissionDTO {
    return PermissionDTO(
        id = this.id.value,
        name = this.name,
        description = this.description,
        categoryId = this.category.id.value // Relación con Category
    )
}