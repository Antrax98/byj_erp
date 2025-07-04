package dev.byjtech.erp.document_management.domain.model


import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class Document(
    val id: Int,
    val documentType: String,
    val documentNumber: String,
    val issueDate: LocalDate,
    val dueDate: LocalDate?,
    val currency: String,
    val netAmount: Double,
    val taxAmount: Double,
    val totalAmount: Double,
    val fileUrl: String,
    val createdBy: UUID,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val active: Boolean,
    val status: DocumentStatus,
    // val companyId: UUID?,
    // val categoryId: Int?
)
