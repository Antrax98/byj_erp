package dev.byjtech.erp.document_management.request

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class UpdateDocumentRequest(
    val documentType: String?,
    val documentNumber: String?,
    val issueDate: LocalDate?,
    val dueDate: LocalDate?,
    val status: String?,
    val currency: String?,
    val netAmount: Double?,
    val taxAmount: Double?,
    val totalAmount: Double?,
    val fileUrl: String?
)
