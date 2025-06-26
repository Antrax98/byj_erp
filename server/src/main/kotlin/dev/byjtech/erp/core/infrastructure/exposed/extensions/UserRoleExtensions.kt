package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.dto.UserRoleDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserRoleEntity
import dev.byjtech.erp.utils.datetime.toKotlinx

fun UserRoleEntity.toDTO(): UserRoleDTO {
    return UserRoleDTO(
        id = this.id.value.toString(),
        userId = this.user.id.value.toString(),
        roleId = this.role.id.value.toString(),
    )
}