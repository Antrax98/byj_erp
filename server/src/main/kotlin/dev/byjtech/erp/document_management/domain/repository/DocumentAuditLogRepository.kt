package dev.byjtech.erp.document_management.domain.repository

import dev.byjtech.erp.document_management.domain.model.DocumentAuditLog
import java.util.UUID

interface DocumentAuditLogRepository {
    fun findByDocumentId(documentId: UUID): List<DocumentAuditLog>
    fun create(log: DocumentAuditLog): DocumentAuditLog
    fun deleteById(id: UUID): Boolean
}
