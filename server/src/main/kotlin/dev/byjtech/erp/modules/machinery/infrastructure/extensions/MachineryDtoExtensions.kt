package dev.byjtech.erp.modules.machinery.infrastructure.extensions

import dev.byjtech.erp.modules.machinery.domain.model.Machinery
import dev.byjtech.erp.modules.machinery.domain.model.MachineryHistory
import dev.byjtech.erp.modules.machinery.dto.MachineryDTO
import dev.byjtech.erp.modules.machinery.dto.MachineryHistoryDTO
import dev.byjtech.erp.modules.machinery.response.MachineryHistoryResponse
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

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

fun MachineryHistory.toHistoryDTO(machineryName: String = "Maquinaria", changedBy: String = "Usuario"): MachineryHistoryDTO {
    // Mapear el field a un tipo de cambio más descriptivo
    val changeType = when (this.field.uppercase()) {
        "CREACION", "CREATE", "CREATION" -> "CREACION"
        "ACTUALIZACION", "UPDATE", "UPDATED" -> "ACTUALIZACION"
        "ACTIVACION", "ACTIVATE", "ACTIVATED" -> "ACTIVACION"
        "DESACTIVACION", "DEACTIVATE", "DEACTIVATED" -> "DESACTIVACION"
        "NOMBRE", "NAME" -> "ACTUALIZACION"
        "DESCRIPCION", "DESCRIPTION" -> "ACTUALIZACION"
        "MARCA", "BRAND" -> "ACTUALIZACION"
        "MODELO", "MODEL" -> "ACTUALIZACION"
        else -> "CAMBIO"
    }
    
    // Crear una descripción más descriptiva
    val changeDescription = when {
        this.comment != null && this.comment.isNotBlank() -> this.comment
        this.field.equals("CREACION", ignoreCase = true) -> "Maquinaria creada"
        this.field.equals("ACTUALIZACION", ignoreCase = true) -> "Información de la maquinaria actualizada"
        this.field.equals("ACTIVACION", ignoreCase = true) -> "Maquinaria activada"
        this.field.equals("DESACTIVACION", ignoreCase = true) -> "Maquinaria desactivada"
        this.oldValue != null && this.newValue != null -> "Campo '${this.field}' modificado"
        else -> "Cambio registrado en ${this.field}"
    }
    
    return MachineryHistoryDTO(
        id = this.id.toString(),
        machineryId = this.machineryId.toString(),
        machineryName = machineryName,
        changeType = changeType,
        changeDescription = changeDescription,
        changedBy = changedBy,
        changeDate = this.createdAt.toInstant(TimeZone.currentSystemDefault()),
        oldValues = this.oldValue,
        newValues = this.newValue
    )
}
