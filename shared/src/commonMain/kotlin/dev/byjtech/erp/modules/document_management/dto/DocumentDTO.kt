package dev.byjtech.erp.document_management.dto

import dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus
import dev.byjtech.erp.modules.document_management.domain.model.DocumentType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class DocumentDTO(
    val id: String,
    val type: DocumentType,
    val documentNumber: String,
    val issueDate: LocalDate,
    val dueDate: LocalDate?,
    val status: DocumentStatus,
    val currency: String,
    val netAmount: Double,
    val taxAmount: Double,
    val totalAmount: Double,
    val fileUrl: String,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val active: Boolean,
    val companyId: String?
)
