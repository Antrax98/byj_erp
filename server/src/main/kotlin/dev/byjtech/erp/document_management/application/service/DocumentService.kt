package dev.byjtech.erp.document_management.application.service

import dev.byjtech.erp.document_management.domain.model.Document
import dev.byjtech.erp.document_management.domain.repository.DocumentRepository
import dev.byjtech.erp.document_management.dto.DocumentDTO
import dev.byjtech.erp.document_management.infrastructure.exposed.extensions.toDTO
import java.util.UUID

class DocumentService(
    private val documentRepository: DocumentRepository
) {
    
    fun getAllDocuments(): List<DocumentDTO> {
        return documentRepository.findAll().map { it.toDTO() }
    }
    
    fun getDocumentById(id: UUID): DocumentDTO? {
        return documentRepository.findById(id)?.toDTO()
    }
    
    fun createDocument(document: Document): DocumentDTO {
        val savedDocument = documentRepository.save(document)
        return savedDocument.toDTO()
    }
    
    fun updateDocument(document: Document): DocumentDTO {
        val updatedDocument = documentRepository.update(document)
        return updatedDocument.toDTO()
    }
    
    fun deleteDocument(id: UUID) {
        documentRepository.delete(id)
    }
}