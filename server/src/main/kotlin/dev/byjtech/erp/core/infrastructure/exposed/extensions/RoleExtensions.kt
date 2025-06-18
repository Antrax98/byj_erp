package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.domain.model.Role
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.PermissionEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.RoleEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import java.util.UUID

fun RoleEntity.toDTO(): RoleDTO {
    return RoleDTO(
        id = this.id.value.toString(),
        name = this.name,
        description = this.description,
        createdAt = this.createdAt?.toKotlinx(),
        updatedAt = this.updatedAt?.toKotlinx(),
        companyId = this.company.id.value.toString()
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
        permissions = permissions?.toMutableSet(),
        companyId = this.company.id.value
    )
}

//si no se le da el id de la compañia, sera uno aleatorio solo para rellenar
fun Role.toDTO(): RoleDTO {
    return RoleDTO(
        id = this.id.toString(),
        name = this.name,
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        companyId = this.companyId.toString()
    )

}