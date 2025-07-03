package dev.byjtech.erp.modules.document_management.domain.repository

import dev.byjtech.erp.modules.document_management.domain.model.DocumentAuditLog

interface DocumentAuditLogRepository {
    fun findByDocumentId(documentId: Int): List<DocumentAuditLog>
    fun save(log: DocumentAuditLog): DocumentAuditLog
}
