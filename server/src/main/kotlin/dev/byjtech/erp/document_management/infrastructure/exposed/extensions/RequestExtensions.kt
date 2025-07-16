package dev.byjtech.erp.document_management.infrastructure.exposed.extensions

import dev.byjtech.erp.document_management.domain.model.Document
import dev.byjtech.erp.document_management.domain.model.DocumentStatus
import dev.byjtech.erp.document_management.request.CreateDocumentRequest
import dev.byjtech.erp.document_management.request.UpdateDocumentRequest
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

// Extensión para convertir CreateDocumentRequest a Document (modelo de dominio)
fun CreateDocumentRequest.toDomain(companyId: UUID, userId: UUID): Document {
    return Document(
        id = UUID.randomUUID(),
        documentType = this.documentType,
        documentNumber = this.documentNumber,
        issueDate = this.issueDate,
        dueDate = this.dueDate,
        status = try {
            DocumentStatus.valueOf(this.status.uppercase())
        } catch (e: IllegalArgumentException) {
            DocumentStatus.UPLOADED // valor por defecto
        },
        currency = this.currency,
        netAmount = this.netAmount,
        taxAmount = this.taxAmount,
        totalAmount = this.totalAmount,
        fileUrl = this.fileUrl,
        createdBy = userId,
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        active = true,
        companyId = companyId
    )
}

// Extensión para aplicar UpdateDocumentRequest a Document existente
fun Document.applyUpdate(request: UpdateDocumentRequest): Document {
    return this.copy(
        documentType = request.documentType ?: this.documentType,
        documentNumber = request.documentNumber ?: this.documentNumber,
        issueDate = request.issueDate ?: this.issueDate,
        dueDate = request.dueDate ?: this.dueDate,
        status = request.status?.let { 
            try {
                DocumentStatus.valueOf(it.uppercase())
            } catch (e: IllegalArgumentException) {
                this.status
            }
        } ?: this.status,
        currency = request.currency ?: this.currency,
        netAmount = request.netAmount ?: this.netAmount,
        taxAmount = request.taxAmount ?: this.taxAmount,
        totalAmount = request.totalAmount ?: this.totalAmount,
        fileUrl = request.fileUrl ?: this.fileUrl,
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )
}
