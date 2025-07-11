package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.dto.PermissionDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.PermissionEntity

fun PermissionEntity.toDTO(): PermissionDTO {
    return PermissionDTO(
        id = this.id.value.toString(),
        name = this.name,
        description = this.description
    )
}

fun PermissionEntity.toModel(): Permission {
    return Permission(
        id = this.id.value,
        name = this.name,
        description = this.description
    )
}