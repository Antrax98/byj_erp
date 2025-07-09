package dev.byjtech.erp.document_management.domain.repository

import dev.byjtech.erp.document_management.domain.model.Document
import java.util.UUID

interface DocumentRepository {
    fun findById(id: UUID): Document?
    fun findAll(): List<Document>
    fun save(document: Document): Document
    fun update(document: Document): Document
    fun delete(id: UUID)
}