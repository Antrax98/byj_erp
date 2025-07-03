package dev.byjtech.erp.modules.document_management.domain.model
    enum class DocumentEventType {
        REACTIVATED,  // Documento reactivado después de estar inactivo
        DEACTIVATED,  // El documento fue desactivado (no se elimina)
        VOIDED;        // Documento anulado (por ejemplo, con nota de crédito)

        companion object {
            fun fromString(value: String): DocumentEventType =
                entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                    ?: throw IllegalArgumentException("Unknown DocumentEventType: $value")
        }
    }