package dev.byjtech.erp.document_management.application.service

import dev.byjtech.erp.document_management.domain.model.Document
import dev.byjtech.erp.document_management.domain.repository.DocumentRepository
import dev.byjtech.erp.document_management.domain.repository.CompanyValidationRepository
import dev.byjtech.erp.document_management.dto.DocumentDTO
import dev.byjtech.erp.document_management.infrastructure.exposed.extensions.toDTO
import dev.byjtech.erp.modules.document_management.request.DocumentSearchRequest
import dev.byjtech.erp.modules.document_management.dto.DocumentSearchResponse
import dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus
import kotlinx.datetime.*
import java.util.UUID

class DocumentService(
    private val documentRepository: DocumentRepository,
    private val companyValidationRepository: CompanyValidationRepository,
    private val documentEditHistoryService: DocumentEditHistoryService,
    private val documentAuditLogService: DocumentAuditLogService
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
    
    fun searchDocuments(companyId: UUID, searchRequest: DocumentSearchRequest): DocumentSearchResponse {
        val searchResult = documentRepository.search(companyId, searchRequest)
        val documentDTOs = searchResult.documents.map { it.toDTO() }
        
        return DocumentSearchResponse.create(
            documents = documentDTOs,
            totalCount = searchResult.totalCount,
            page = searchRequest.page,
            pageSize = searchRequest.pageSize
        )
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
    
    fun updateDocument(documentId: UUID, updatedDocument: Document, companyId: UUID, userId: UUID): DocumentDTO? {
        val existingDocument = documentRepository.findById(documentId)
        
        if (existingDocument == null || existingDocument.companyId != companyId) {
            return null
        }
        
        // Validar que el documento puede ser editado según las reglas de negocio DDD
        existingDocument.validateCanBeEdited()
        
        // Lógica de negocio: Validaciones
        validateDocumentBusinessRules(updatedDocument)
        
        // Registrar historial de cambios ANTES de actualizar
        documentEditHistoryService.recordDocumentChanges(
            oldDocument = existingDocument,
            newDocument = updatedDocument,
            userId = userId
        )
        
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
    
    fun deactivateDocument(documentId: UUID, companyId: UUID, userId: UUID, reason: String? = null): DocumentDTO? {
        val existingDocument = documentRepository.findById(documentId)
        
        // Validar que el documento existe y pertenece a la compañía
        if (existingDocument == null || existingDocument.companyId != companyId) {
            return null
        }
        
        // Validar reglas de negocio para desactivación
        existingDocument.validateCanBeDeactivated()
        
        // Crear documento con estado DEACTIVATED
        val deactivatedDocument = existingDocument.copy(
            status = DocumentStatus.DEACTIVATED,
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
        
        // Registrar cambio en el historial de edición
        documentEditHistoryService.recordDocumentChanges(
            oldDocument = existingDocument,
            newDocument = deactivatedDocument,
            userId = userId
        )
        
        // Guardar documento actualizado
        val savedDocument = documentRepository.update(deactivatedDocument)
        
        // Registrar evento de auditoría
        documentAuditLogService.logDocumentDeactivated(
            documentId = documentId,
            userId = userId,
            reason = reason
        )
        
        return savedDocument.toDTO()
    }
    
    // Lógica de negocio: Validaciones
    private fun validateDocumentBusinessRules(document: Document) {
        // Validar que la compañía existe en la base de datos core
        if (document.companyId != null && !companyValidationRepository.existsById(document.companyId)) {
            throw IllegalArgumentException("Company with ID '${document.companyId}' does not exist")
        }
        
        // Validar que el usuario que creó el documento existe en la base de datos core
        if (!companyValidationRepository.userExistsById(document.createdBy)) {
            throw IllegalArgumentException("User with ID '${document.createdBy}' does not exist")
        }
        
        // Validar fechas
        if (document.dueDate != null && document.dueDate < document.issueDate) {
            throw IllegalArgumentException("Due date cannot be earlier than issue date")
        }
        
        // Validar que la fecha de vencimiento no sea anterior a la fecha actual
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        if (document.dueDate != null && document.dueDate < today) {
            throw IllegalArgumentException("Due date cannot be earlier than today")
        }
        
        if (document.netAmount < 0) {
            throw IllegalArgumentException("Net amount cannot be negative")
        }
        
        if (document.taxAmount < 0) {
            throw IllegalArgumentException("Tax amount cannot be negative")
        }
        
        // Redondear los valores a 2 decimales antes de comparar para evitar problemas de precisión
        val roundedNetAmount = kotlin.math.round(document.netAmount * 100) / 100
        val roundedTaxAmount = kotlin.math.round(document.taxAmount * 100) / 100
        val roundedTotalAmount = kotlin.math.round(document.totalAmount * 100) / 100
        val expectedTotal = kotlin.math.round((roundedNetAmount + roundedTaxAmount) * 100) / 100
        
        if (roundedTotalAmount != expectedTotal) {
            throw IllegalArgumentException(
                "Total amount ($roundedTotalAmount) must equal net amount ($roundedNetAmount) plus tax amount ($roundedTaxAmount). Expected: $expectedTotal"
            )
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
        if (document.status == DocumentStatus.APPROVED) {
            throw IllegalArgumentException("Cannot delete approved documents")
        }
    }
}