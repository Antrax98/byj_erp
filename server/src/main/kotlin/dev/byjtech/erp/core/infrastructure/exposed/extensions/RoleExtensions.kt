package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.domain.model.Role
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.PermissionEntity
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

fun RoleEntity.toModel(permissionsSet: Set<PermissionEntity>? = null): Role {
    val permissions = permissionsSet?.map { it.toModel() }?.toSet()
    return Role(
        id = this.id.value,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt?.toKotlinx(),
        updatedAt = this.updatedAt?.toKotlinx(),
        permissions = permissions?.toMutableSet()
    )
}

//company es 0 hasta que se me ocurra como lo voy a manejar
fun Role.toDTO(): RoleDTO {
    return RoleDTO(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        companyId = 0
    )

}