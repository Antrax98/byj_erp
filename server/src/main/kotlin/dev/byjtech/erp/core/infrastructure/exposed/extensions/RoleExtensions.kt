package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.RoleEntity
import dev.byjtech.erp.utils.datetime.toKotlinx

fun RoleEntity.toDTO(): RoleDTO {
    return RoleDTO(
        id = this.id.value,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt?.toKotlinx(),
        updatedAt = this.updatedAt?.toKotlinx(),
        companyId = this.company.id.value
    )
}