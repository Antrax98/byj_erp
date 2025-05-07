package dev.byjtech.erp.core.database.superAdmins

import dev.byjtech.erp.core.dto.superAdmin.SuperAdminDTO

fun SuperAdminEntity.toDTO(): SuperAdminDTO {
    return SuperAdminDTO(
        id = this.id.value,
        userId = this.userId.value,
        description = this.description
    )
}