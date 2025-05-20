package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.dto.UserRoleDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserRoleEntity
import dev.byjtech.erp.utils.datetime.toKotlinx

fun UserRoleEntity.toDTO(): UserRoleDTO {
    return UserRoleDTO(
        id = this.id.value,
        userId = this.user.id.value,
        roleId = this.role.id.value,
    )
}