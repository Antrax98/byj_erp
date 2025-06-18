package dev.byjtech.erp.modules.document_management.domain.model

enum class DocumentEventType {
    DEACTIVATED,  // El documento fue desactivado (no se elimina)
    REACTIVATED,  // Documento reactivado después de estar inactivo
    VOIDED        // Documento anulado (por ejemplo, con nota de crédito)
}
