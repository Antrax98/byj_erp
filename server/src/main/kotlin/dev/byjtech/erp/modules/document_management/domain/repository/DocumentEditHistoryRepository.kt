package dev.byjtech.erp.document_management.domain.repository

import dev.byjtech.erp.document_management.domain.model.DocumentEditHistory
import java.util.UUID

interface DocumentEditHistoryRepository {
    fun findByDocumentId(documentId: UUID): List<DocumentEditHistory>
    fun findByCompanyId(companyId: UUID): List<DocumentEditHistory>
    fun save(history: DocumentEditHistory): DocumentEditHistory
}
