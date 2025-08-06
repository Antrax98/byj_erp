package dev.byjtech.erp.machinery.infrastructure.extensions

import dev.byjtech.erp.machinery.domain.model.Machinery
import dev.byjtech.erp.machinery.domain.model.MachineryHistory
import dev.byjtech.erp.modules.machinery.dto.MachineryDTO
import dev.byjtech.erp.modules.machinery.response.MachineryHistoryResponse

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
        updatedAt = this.updatedAt,
        isActive = this.isActive
    )
}

fun List<Machinery>.toDTO(): List<MachineryDTO> {
    return this.map { it.toDTO() }
}

fun MachineryHistory.toDTO(): MachineryHistoryResponse {
    return MachineryHistoryResponse(
        id = this.id.toString(),
        machineryId = this.machineryId.toString(),
        userId = this.userId.toString(),
        field = this.field,
        oldValue = this.oldValue,
        newValue = this.newValue,
        createdAt = this.createdAt.toString(),
        comment = this.comment
    )
}
