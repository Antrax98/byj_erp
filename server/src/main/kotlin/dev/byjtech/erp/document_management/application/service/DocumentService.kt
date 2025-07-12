package dev.byjtech.erp.document_management.application.service

import dev.byjtech.erp.document_management.domain.model.Document
import dev.byjtech.erp.document_management.domain.repository.DocumentRepository
import dev.byjtech.erp.document_management.dto.DocumentDTO
import dev.byjtech.erp.document_management.infrastructure.exposed.extensions.toDTO
import java.util.UUID

class DocumentService(
    private val documentRepository: DocumentRepository
) {
    
    // Método para SuperAdmins: obtener TODOS los documentos sin filtro por compañía
    fun getAllDocuments(): List<DocumentDTO> {
        val documents = documentRepository.findAll()
        return documents.map { it.toDTO() }
    }
    
    fun getAllDocumentsByCompany(companyId: UUID): List<DocumentDTO> {
        val documents = documentRepository.findByCompanyId(companyId)
        return documents.map { it.toDTO() }
    }
    
    fun getDocumentById(documentId: UUID, companyId: UUID): DocumentDTO? {
        val document = documentRepository.findById(documentId)
        return if (document != null && document.companyId == companyId) {
            document.toDTO()
        } else {
            null
        }
    }
    
    fun createDocument(document: Document): DocumentDTO {
        // Lógica de negocio: Validaciones
        validateDocumentBusinessRules(document)
        
        val savedDocument = documentRepository.save(document)
        return savedDocument.toDTO()
    }
    
    fun updateDocument(documentId: UUID, updatedDocument: Document, companyId: UUID): DocumentDTO? {
        val existingDocument = documentRepository.findById(documentId)
        
        if (existingDocument == null || existingDocument.companyId != companyId) {
            return null
        }
        
        // Lógica de negocio: Validaciones
        validateDocumentBusinessRules(updatedDocument)
        
        val savedDocument = documentRepository.update(updatedDocument)
        return savedDocument.toDTO()
    }
    
    fun deleteDocument(documentId: UUID, companyId: UUID): Boolean {
        val existingDocument = documentRepository.findById(documentId)
        
        if (existingDocument == null || existingDocument.companyId != companyId) {
            return false
        }
        
        // Lógica de negocio: Validar si se puede eliminar
        validateDocumentDeletion(existingDocument)
        
        documentRepository.delete(documentId)
        return true
    }
    
    // Lógica de negocio: Validaciones
    private fun validateDocumentBusinessRules(document: Document) {
        if (document.netAmount < 0) {
            throw IllegalArgumentException("Net amount cannot be negative")
        }
        
        if (document.taxAmount < 0) {
            throw IllegalArgumentException("Tax amount cannot be negative")
        }
        
        if (document.totalAmount != document.netAmount + document.taxAmount) {
            throw IllegalArgumentException("Total amount must equal net amount plus tax amount")
        }
        
        if (document.documentNumber.isBlank()) {
            throw IllegalArgumentException("Document number cannot be empty")
        }
        
        // Verificar duplicados de número de documento en la misma empresa
        val existingDocuments = documentRepository.findByCompanyId(document.companyId!!)
        val duplicateNumber = existingDocuments.any { 
            it.documentNumber == document.documentNumber && it.id != document.id 
        }
        
        if (duplicateNumber) {
            throw IllegalArgumentException("Document number '${document.documentNumber}' already exists in this company")
        }
    }
    
    private fun validateDocumentDeletion(document: Document) {
        // No permitir eliminar documentos aprobados
        if (document.status.name == "APPROVED") {
            throw IllegalArgumentException("Cannot delete approved documents")
        }
    }
}