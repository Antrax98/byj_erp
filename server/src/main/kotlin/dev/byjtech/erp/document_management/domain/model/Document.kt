package dev.byjtech.erp.document_management.domain.model

import dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus
import dev.byjtech.erp.modules.document_management.domain.model.DocumentType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class Document(
    val id: UUID,
    val type: DocumentType,
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
    val companyId: UUID?
) {
    fun validateCanBeEdited() {
        if (type == DocumentType.INVOICE) {
            throw IllegalStateException("Las facturas no se pueden editar")
        }
    }
    
    fun validateCanBeDeactivated() {
        if (status != DocumentStatus.UPLOADED && status != DocumentStatus.REJECTED) {
            throw IllegalStateException("Solo se pueden desactivar documentos en estado UPLOADED o REJECTED. Estado actual: ${status.name}")
        }
        
        if (status == DocumentStatus.DEACTIVATED) {
            throw IllegalStateException("El documento ya está desactivado")
        }
    }
}
