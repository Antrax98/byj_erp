package dev.byjtech.erp.modules.document_management.utils

import dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus
import kotlinx.datetime.*

/**
 * Obtiene el nombre para mostrar de un estado de documento
 */
fun getStatusDisplayName(status: DocumentStatus): String {
    return when (status) {
        DocumentStatus.UPLOADED -> "Cargado"
        DocumentStatus.SENT -> "Enviado"
        DocumentStatus.APPROVED -> "Aprobado"
        DocumentStatus.REJECTED -> "Rechazado"
        DocumentStatus.DEACTIVATED -> "Desactivado"
    }
}

/**
 * Verifica si un documento está vencido
 */
fun isDocumentOverdue(dueDate: LocalDate?, status: DocumentStatus): Boolean {
    if (dueDate == null || status == DocumentStatus.APPROVED) {
        return false
    }
    
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return dueDate < today
}

/**
 * Data class para representar las acciones disponibles
 */
data class DocumentActions(
    val canSend: Boolean = false,
    val canApprove: Boolean = false,
    val canReject: Boolean = false,
    val canEdit: Boolean = false,
    val canDelete: Boolean = false,
    val canView: Boolean = true,
    val canDownload: Boolean = false
)

/**
 * Obtiene las acciones disponibles para un documento según su estado y rol del usuario
 */
fun getAvailableActions(status: DocumentStatus, userRole: String = "USER"): DocumentActions {
    return when (status) {
        DocumentStatus.UPLOADED -> DocumentActions(
            canSend = true,
            canEdit = true,
            canDelete = true,
            canView = true
        )
        DocumentStatus.SENT -> DocumentActions(
            canApprove = userRole == "ADMIN" || userRole == "MANAGER",
            canReject = userRole == "ADMIN" || userRole == "MANAGER",
            canView = true
        )
        DocumentStatus.APPROVED -> DocumentActions(
            canView = true,
            canDownload = true
        )
        DocumentStatus.REJECTED -> DocumentActions(
            canEdit = true,
            canView = true,
            canSend = true // Puede reenviar después de editar
        )
        DocumentStatus.DEACTIVATED -> DocumentActions(
            canView = true // Solo puede visualizar documentos desactivados
        )
    }
}

/**
 * Verifica si un documento puede ser desactivado según su estado
 */
fun canBeDeactivated(status: DocumentStatus): Boolean {
    return status != DocumentStatus.DEACTIVATED // No puede desactivar un documento ya desactivado
}

/**
 * Obtiene el mensaje de explicación de por qué un documento no puede ser desactivado
 */
fun getDeactivationRestrictionMessage(status: DocumentStatus): String? {
    return when (status) {
        DocumentStatus.UPLOADED, DocumentStatus.REJECTED -> null
        DocumentStatus.SENT -> "No se pueden desactivar documentos enviados"
        DocumentStatus.APPROVED -> "No se pueden desactivar documentos aprobados"
        DocumentStatus.DEACTIVATED -> "El documento ya está desactivado"
    }
}
