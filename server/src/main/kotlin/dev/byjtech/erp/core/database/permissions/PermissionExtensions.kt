package dev.byjtech.erp.core.database.permissions

import dev.byjtech.erp.core.dto.permission.PermissionDTO

fun dev.byjtech.erp.core.database.permissions.PermissionEntity.toDTO(): dev.byjtech.erp.core.dto.permission.PermissionDTO {
    return dev.byjtech.erp.core.dto.permission.PermissionDTO(
        id = this.id.value,
        name = this.name,
        description = this.description,
        categoryId = this.category.id.value // Relación con Category
    )
}