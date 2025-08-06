package dev.byjtech.erp.document_management.domain.repository

import dev.byjtech.erp.document_management.domain.model.Document
import dev.byjtech.erp.modules.document_management.request.DocumentSearchRequest
import java.util.UUID

data class SearchResult(
    val documents: List<Document>,
    val totalCount: Long
)

interface DocumentRepository {
    fun findById(id: UUID): Document?
    fun findAll(): List<Document>
    fun findByCompanyId(companyId: UUID): List<Document>
    fun search(companyId: UUID, searchRequest: DocumentSearchRequest): SearchResult
    fun save(document: Document): Document
    fun update(document: Document): Document
    fun delete(id: UUID)
}