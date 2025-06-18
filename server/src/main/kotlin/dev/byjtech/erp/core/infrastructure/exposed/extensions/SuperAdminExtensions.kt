package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.domain.model.SuperAdmin
import dev.byjtech.erp.core.dto.SuperAdminDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.SuperAdminEntity

//fun SuperAdminEntity.toDTO(): SuperAdminDTO {
//    return SuperAdminDTO(
//        id = this.id.value.toString(),
//        userId = this.userId.value.toString(),
//        description = this.description
//    )
//}

fun SuperAdminEntity.toModel(): SuperAdmin {
    return SuperAdmin(
        id = this.id.value,
        userId = this.user.id.value,
        description = this.description
    )
}

fun SuperAdmin.toDTO(): SuperAdminDTO {
    return SuperAdminDTO(
        id = this.id.toString(),
        userId = this.userId.toString(),
        description = this.description
    )
}