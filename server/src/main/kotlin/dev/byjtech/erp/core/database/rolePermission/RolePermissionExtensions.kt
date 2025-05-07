package dev.byjtech.erp.core.database.rolePermission

import dev.byjtech.erp.core.dto.rolePermission.RolePermissionDTO
import dev.byjtech.erp.utils.datetime.toKotlinx

fun dev.byjtech.erp.core.database.rolePermission.RolePermissionEntity.toDTO(): dev.byjtech.erp.core.dto.rolePermission.RolePermissionDTO {
    return dev.byjtech.erp.core.dto.rolePermission.RolePermissionDTO(
        id = this.id.value,
        roleId = this.role.id.value,
        permissionId = this.permission.id.value,
        createdAt = this.createdAt.toKotlinx(),
        createdBy = this.createdBy?.id?.value
    )
}