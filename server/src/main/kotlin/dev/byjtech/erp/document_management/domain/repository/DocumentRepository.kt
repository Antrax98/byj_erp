package dev.byjtech.erp.document_management.domain.repository
import dev.byjtech.erp.document_management.domain.model.Document

interface DocumentRepository {
    fun findById(id: Int): Document?
    fun findAll(): List<Document>
    fun save(document: Document): Document
    fun update(document: Document): Document
    fun delete(id: Int)
}