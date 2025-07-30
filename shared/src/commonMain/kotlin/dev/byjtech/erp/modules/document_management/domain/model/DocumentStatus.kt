package dev.byjtech.erp.modules.document_management.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class DocumentStatus {
    UPLOADED,    // Documento cargado, aún no enviado
    SENT,        // Documento enviado para revisión u otro proceso
    APPROVED,    // Documento aprobado
    REJECTED,    // Documento rechazado (puede ser editado y reenviado si no es factura)
    DEACTIVATED  // Documento desactivado
}
