package dev.byjtech.erp.document_management.utils

import dev.byjtech.erp.document_management.enums.DocumentStatus

data class DocumentActions(
    val canSend: Boolean,
    val canApprove: Boolean,
    val canReject: Boolean,
    val canEdit: Boolean,
    val canDelete: Boolean
)

fun getAvailableActions(currentStatus: String, userRole: String = "USER"): DocumentActions {
    val status = try {
        DocumentStatus.valueOf(currentStatus.uppercase())
    } catch (e: IllegalArgumentException) {
        DocumentStatus.UPLOADED
    }
    
    return when (status) {
        DocumentStatus.UPLOADED -> DocumentActions(
            canSend = true,  // Usuario puede enviar para revisión
            canApprove = false,
            canReject = false,
            canEdit = true,  // Solo se puede editar cuando está UPLOADED
            canDelete = true  // Solo se puede eliminar cuando está UPLOADED
        )
        
        DocumentStatus.SENT -> DocumentActions(
            canSend = false,
            canApprove = userRole == "ADMIN" || userRole == "SUPERVISOR", // Solo supervisores pueden aprobar
            canReject = userRole == "ADMIN" || userRole == "SUPERVISOR",  // Solo supervisores pueden rechazar
            canEdit = false,  // No se puede editar mientras está en revisión
            canDelete = false
        )
        
        DocumentStatus.APPROVED -> DocumentActions(
            canSend = false,
            canApprove = false,
            canReject = false,
            canEdit = false,  // Documentos aprobados no se pueden editar
            canDelete = false  // Documentos aprobados no se pueden eliminar
        )
        
        DocumentStatus.REJECTED -> DocumentActions(
            canSend = true,  // Se puede reenviar después de correcciones
            canApprove = false,
            canReject = false,
            canEdit = true,  // Se puede editar para corregir y reenviar
            canDelete = true  // Se puede eliminar si ya no se necesita
        )
    }
}

fun getStatusDisplayName(status: String): String {
    return when (status.uppercase()) {
        "UPLOADED" -> "Cargado"
        "SENT" -> "Enviado"
        "APPROVED" -> "Aprobado"
        "REJECTED" -> "Rechazado"
        else -> status
    }
}

fun getStatusColor(status: String): String {
    return when (status.uppercase()) {
        "UPLOADED" -> "#FFA726" // Orange
        "SENT" -> "#42A5F5" // Blue
        "APPROVED" -> "#66BB6A" // Green
        "REJECTED" -> "#EF5350" // Red
        else -> "#9E9E9E" // Gray
    }
}
