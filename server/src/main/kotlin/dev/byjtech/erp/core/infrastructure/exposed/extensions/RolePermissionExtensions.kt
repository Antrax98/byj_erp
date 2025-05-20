package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.dto.RolePermissionDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.RolePermissionEntity
import dev.byjtech.erp.utils.datetime.toKotlinx

fun RolePermissionEntity.toDTO(): RolePermissionDTO {
    return RolePermissionDTO(
        id = this.id.value,
        roleId = this.role.id.value,
        permissionId = this.permission.id.value,
        createdAt = this.createdAt?.toKotlinx(),
    )
}