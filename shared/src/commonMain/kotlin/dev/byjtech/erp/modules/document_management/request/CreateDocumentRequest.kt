package dev.byjtech.erp.document_management.request

import dev.byjtech.erp.modules.document_management.domain.model.DocumentType
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CreateDocumentRequest(
    val type: DocumentType,
    val documentNumber: String,
    val issueDate: LocalDate,
    val dueDate: LocalDate?,
    val status: String,
    val currency: String,
    val netAmount: Double,
    val taxAmount: Double,
    val totalAmount: Double,
    val fileUrl: String
)
