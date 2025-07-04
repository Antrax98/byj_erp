package dev.byjtech.erp.document_management.domain.model


enum class DocumentStatus {
    UPLOADED,  // Documento cargado, aún no enviado
    SENT,      // Documento enviado para revisión u otro proceso
    APPROVED,  // Documento aprobado
    REJECTED   // Documento rechazado (puede ser editado y reenviado si no es factura)
}
