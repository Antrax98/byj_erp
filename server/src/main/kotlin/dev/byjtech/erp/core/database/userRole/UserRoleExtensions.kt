package dev.byjtech.erp.core.database.userRole

import dev.byjtech.erp.core.dto.userRole.UserRoleDTO
import dev.byjtech.erp.utils.datetime.toKotlinx

fun dev.byjtech.erp.core.database.userRole.UserRoleEntity.toDTO(): dev.byjtech.erp.core.dto.userRole.UserRoleDTO {
    return dev.byjtech.erp.core.dto.userRole.UserRoleDTO(
        id = this.id.value,
        userId = this.user.id.value,
        roleId = this.role.id.value,
        createdAt = this.createdAt.toKotlinx(),
        createdBy = this.createdBy?.id?.value
    )
}