package dev.byjtech.erp.machinery.infrastructure.extensions

import dev.byjtech.erp.machinery.domain.model.Machinery
import dev.byjtech.erp.modules.machinery.dto.MachineryDTO

fun Machinery.toDTO(): MachineryDTO {
    return MachineryDTO(
        id = this.id.toString(),
        name = this.name,
        model = this.model,
        manufacturer = this.brand,
        serialNumber = this.serialNumber,
        status = this.status,
        location = this.location,
        description = this.description,
        purchaseDate = null,
        warrantyExpirationDate = null,
        lastMaintenanceDate = null,
        nextMaintenanceDate = null,
        companyId = this.companyId.toString(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun List<Machinery>.toDTO(): List<MachineryDTO> {
    return this.map { it.toDTO() }
}
