package dev.byjtech.erp.modules.document_management.domain.model

import kotlinx.serialization.Serializable


@Serializable
enum class DocumentType {
    INVOICE,        // Factura
    CREDIT_NOTE,    // Nota de crédito
    RECEIPT         // Boleta
}

fun getDocumentTypeDisplayName(type: DocumentType): String {
    return when (type) {
        DocumentType.INVOICE -> "Factura"
        DocumentType.CREDIT_NOTE -> "Nota de Crédito"
        DocumentType.RECEIPT -> "Boleta"
    }
}
