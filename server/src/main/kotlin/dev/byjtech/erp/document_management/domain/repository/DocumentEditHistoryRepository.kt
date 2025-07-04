package dev.byjtech.erp.document_management.domain.repository
import dev.byjtech.erp.document_management.domain.model.DocumentEditHistory

interface DocumentEditHistoryRepository {
    fun findByDocumentId(documentId: Int): List<DocumentEditHistory>
    fun save(history: DocumentEditHistory): DocumentEditHistory
}
