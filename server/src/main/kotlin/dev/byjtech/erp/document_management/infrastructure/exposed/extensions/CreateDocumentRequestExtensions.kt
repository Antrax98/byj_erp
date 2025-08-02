package dev.byjtech.erp.document_management.infrastructure.exposed.extensions

import dev.byjtech.erp.document_management.domain.model.Document
import dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus
import dev.byjtech.erp.document_management.request.CreateDocumentRequest
import dev.byjtech.erp.document_management.request.UpdateDocumentRequest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

fun CreateDocumentRequest.toDomain(companyId: UUID, userId: UUID): Document {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val documentStatus = try {
        DocumentStatus.valueOf(this.status.uppercase())
    } catch (e: IllegalArgumentException) {
        val validStatuses = DocumentStatus.entries.joinToString(", ") { it.name }
        throw IllegalArgumentException(
            "Invalid document status: '${this.status}'. Valid statuses are: $validStatuses"
        )
    }

    return Document(
        id = UUID.randomUUID(),
        type = this.type,
        documentNumber = this.documentNumber,
        companyId = companyId, // ✅ se inyecta correctamente
        issueDate = this.issueDate,
        dueDate = this.dueDate,
        status = documentStatus, // ✅ status validado con mensaje claro
        currency = this.currency,
        netAmount = this.netAmount,
        taxAmount = this.taxAmount,
        totalAmount = this.totalAmount,
        fileUrl = this.fileUrl,
        createdBy = userId,
        createdAt = now,
        updatedAt = now,
        active = true
    )
}

fun Document.applyUpdate(request: UpdateDocumentRequest): Document {
    return this.copy(
        type = request.type ?: this.type,
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
