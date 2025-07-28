package dev.byjtech.erp.document_management.infrastructure.exposed.extensions

import dev.byjtech.erp.document_management.domain.model.Document
import dev.byjtech.erp.document_management.domain.model.DocumentEditHistory
import dev.byjtech.erp.document_management.domain.model.DocumentAuditLog
import dev.byjtech.erp.document_management.dto.DocumentDTO
import dev.byjtech.erp.document_management.dto.DocumentEditHistoryDTO
import dev.byjtech.erp.document_management.dto.DocumentAuditLogDTO
import dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus

// Extensión para convertir Document (modelo de dominio) a DocumentDTO
fun Document.toDTO(): DocumentDTO {
    return DocumentDTO(
        id = this.id.toString(),
        type = this.type,
        documentNumber = this.documentNumber,
        issueDate = this.issueDate,
        dueDate = this.dueDate,
        status = this.status, // Pasar el enum directamente
        currency = this.currency,
        netAmount = this.netAmount,
        taxAmount = this.taxAmount,
        totalAmount = this.totalAmount,
        fileUrl = this.fileUrl,
        createdBy = this.createdBy.toString(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        active = this.active,
        companyId = this.companyId?.toString()
    )
}

// Extensión para convertir DocumentEditHistory a DocumentEditHistoryDTO
fun DocumentEditHistory.toDTO(): DocumentEditHistoryDTO {
    return DocumentEditHistoryDTO(
        id = this.id.toString(),
        documentId = this.documentId.toString(),
        fieldName = this.fieldName,
        oldValue = this.oldValue,
        newValue = this.newValue,
        userId = this.userId.toString(),
        createdAt = this.createdAt
    )
}

// Extensión para convertir DocumentAuditLog a DocumentAuditLogDTO  
fun DocumentAuditLog.toDTO(): DocumentAuditLogDTO {
    return DocumentAuditLogDTO(
        id = this.id.toString(),
        documentId = this.documentId.toString(),
        eventType = this.eventType.name, // Convertir enum a string
        description = this.description,
        userId = this.userId.toString(),
        createdAt = this.createdAt
    )
}

// Extensión para convertir DocumentDTO a Document (modelo de dominio)
fun DocumentDTO.toDomain(): dev.byjtech.erp.document_management.domain.model.Document {
    return dev.byjtech.erp.document_management.domain.model.Document(
        id = java.util.UUID.fromString(this.id),
        type = this.type,
        documentNumber = this.documentNumber,
        issueDate = this.issueDate,
        dueDate = this.dueDate,
        status = this.status,
        currency = this.currency,
        netAmount = this.netAmount,
        taxAmount = this.taxAmount,
        totalAmount = this.totalAmount,
        fileUrl = this.fileUrl,
        createdBy = java.util.UUID.fromString(this.createdBy),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        active = this.active,
        companyId = this.companyId?.let { java.util.UUID.fromString(it) }
    )
}
