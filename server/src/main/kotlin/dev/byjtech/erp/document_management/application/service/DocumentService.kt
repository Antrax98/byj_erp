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
    
    //se obtienen todos los documentos del sistema
    fun getAllDocuments(): List<DocumentDTO> {
        val documents = documentRepository.findAll()
        return documents.map { it.toDTO() }
    }
    
    //se obtienen documentos por empresa específica
    fun getAllDocumentsByCompany(companyId: UUID): List<DocumentDTO> {
        val documents = documentRepository.findByCompanyId(companyId)
        return documents.map { it.toDTO() }
    }
    
    //se realiza búsqueda de documentos con filtros y paginación
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
    
    //se obtiene un documento específico por ID y empresa
    fun getDocumentById(documentId: UUID, companyId: UUID): DocumentDTO? {
        val document = documentRepository.findById(documentId)
        return if (document != null && document.companyId == companyId) {
            document.toDTO()
        } else {
            null
        }
    }
    
    //se crea un nuevo documento con validaciones de negocio
    fun createDocument(document: Document): DocumentDTO {
        validateDocumentBusinessRules(document)
        
        val savedDocument = documentRepository.save(document)
        return savedDocument.toDTO()
    }
    
    //se actualiza un documento existente con registro de historial
    fun updateDocument(documentId: UUID, updatedDocument: Document, companyId: UUID, userId: UUID): DocumentDTO? {
        val existingDocument = documentRepository.findById(documentId)
        
        if (existingDocument == null || existingDocument.companyId != companyId) {
            return null
        }
        
        existingDocument.validateCanBeEdited()
        
        validateDocumentBusinessRules(updatedDocument)
        
        //se registra el historial de cambios
        documentEditHistoryService.recordDocumentChanges(
            oldDocument = existingDocument,
            newDocument = updatedDocument,
            userId = userId
        )
        
        val savedDocument = documentRepository.update(updatedDocument)
        return savedDocument.toDTO()
    }
    
    //se actualiza solo el estado de un documento
    fun updateDocumentStatus(documentId: UUID, newStatus: DocumentStatus, companyId: UUID, userId: UUID): DocumentDTO? {
        val existingDocument = documentRepository.findById(documentId)
        
        if (existingDocument == null || existingDocument.companyId != companyId) {
            return null
        }
        
        val updatedDocument = existingDocument.copy(
            status = newStatus,
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
        
        //se registra el cambio de estado en el historial
        documentEditHistoryService.recordDocumentChanges(
            oldDocument = existingDocument,
            newDocument = updatedDocument,
            userId = UUID.randomUUID() // TODO: Obtener del contexto de sesión
        )
        
        val savedDocument = documentRepository.update(updatedDocument)
        return savedDocument.toDTO()
    }
    
    //se elimina definitivamente un documento
    fun deleteDocument(documentId: UUID, companyId: UUID): Boolean {
        val existingDocument = documentRepository.findById(documentId)
        
        if (existingDocument == null || existingDocument.companyId != companyId) {
            return false
        }
        
        validateDocumentDeletion(existingDocument)
        
        documentRepository.delete(documentId)
        return true
    }
    
    //se desactiva un documento (soft delete) con registro de auditoría
    fun deactivateDocument(documentId: UUID, companyId: UUID, userId: UUID, reason: String? = null): DocumentDTO? {
        val existingDocument = documentRepository.findById(documentId)
        
        if (existingDocument == null || existingDocument.companyId != companyId) {
            return null
        }
        
        existingDocument.validateCanBeDeactivated()
        
        val deactivatedDocument = existingDocument.copy(
            status = DocumentStatus.DEACTIVATED,
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
        
        //se registra el cambio en el historial
        documentEditHistoryService.recordDocumentChanges(
            oldDocument = existingDocument,
            newDocument = deactivatedDocument,
            userId = userId
        )
        
        val savedDocument = documentRepository.update(deactivatedDocument)
        
        //se registra la desactivación en el log de auditoría
        documentAuditLogService.logDocumentDeactivated(
            documentId = documentId,
            userId = userId,
            reason = reason
        )
        return savedDocument.toDTO()
    }
    
    //se validan las reglas de negocio para un documento
    private fun validateDocumentBusinessRules(document: Document) {
        //se valida que la empresa existe
        if (document.companyId != null && !companyValidationRepository.existsById(document.companyId)) {
            throw IllegalArgumentException("Company with ID '${document.companyId}' does not exist")
        }
        
        //se valida que el usuario existe
        if (!companyValidationRepository.userExistsById(document.createdBy)) {
            throw IllegalArgumentException("User with ID '${document.createdBy}' does not exist")
        }
        
        //se valida que fecha de vencimiento no sea anterior a fecha de emisión
        if (document.dueDate != null && document.dueDate < document.issueDate) {
            throw IllegalArgumentException("Due date cannot be earlier than issue date")
        }
        
        //se valida que fecha de vencimiento no sea anterior al día actual
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        if (document.dueDate != null && document.dueDate < today) {
            throw IllegalArgumentException("Due date cannot be earlier than today")
        }
        
        //se valida que monto neto no sea negativo
        if (document.netAmount < 0) {
            throw IllegalArgumentException("Net amount cannot be negative")
        }
        
        //se valida que monto de impuestos no sea negativo
        if (document.taxAmount < 0) {
            throw IllegalArgumentException("Tax amount cannot be negative")
        }
        
        //se valida que el total coincida con la suma de neto más impuestos
        val roundedNetAmount = kotlin.math.round(document.netAmount * 100) / 100
        val roundedTaxAmount = kotlin.math.round(document.taxAmount * 100) / 100
        val roundedTotalAmount = kotlin.math.round(document.totalAmount * 100) / 100
        val expectedTotal = kotlin.math.round((roundedNetAmount + roundedTaxAmount) * 100) / 100
        
        if (roundedTotalAmount != expectedTotal) {
            throw IllegalArgumentException(
                "Total amount ($roundedTotalAmount) must equal net amount ($roundedNetAmount) plus tax amount ($roundedTaxAmount). Expected: $expectedTotal"
            )
        }
        
        //se valida que el número de documento no esté vacío
        if (document.documentNumber.isBlank()) {
            throw IllegalArgumentException("Document number cannot be empty")
        }
        
        //se valida que no exista duplicado de número de documento en la empresa
        val existingDocuments = documentRepository.findByCompanyId(document.companyId!!)
        val duplicateNumber = existingDocuments.any { 
            it.documentNumber == document.documentNumber && it.id != document.id 
        }
        
        if (duplicateNumber) {
            throw IllegalArgumentException("Document number '${document.documentNumber}' already exists in this company")
        }
    }
    
    //se valida si un documento puede ser eliminado
    private fun validateDocumentDeletion(document: Document) {
        if (document.status == DocumentStatus.APPROVED) {
            throw IllegalArgumentException("Cannot delete approved documents")
        }
    }
}