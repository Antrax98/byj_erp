package dev.byjtech.erp.core.database.roles

import dev.byjtech.erp.core.dto.role.RoleDTO
import dev.byjtech.erp.utils.datetime.toKotlinx

fun dev.byjtech.erp.core.database.roles.RoleEntity.toDTO(): dev.byjtech.erp.core.dto.role.RoleDTO {
    return dev.byjtech.erp.core.dto.role.RoleDTO(
        id = this.id.value,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt.toKotlinx(),
        createdBy = this.createdBy?.id?.value,
        updatedAt = this.updatedAt.toKotlinx(),
        updatedBy = this.updatedBy?.id?.value,
        companyId = this.company.id.value
    )
}