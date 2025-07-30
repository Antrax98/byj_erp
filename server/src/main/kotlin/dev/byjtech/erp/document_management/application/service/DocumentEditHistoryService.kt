package dev.byjtech.erp.document_management.application.service

import dev.byjtech.erp.document_management.domain.model.Document
import dev.byjtech.erp.document_management.domain.model.DocumentEditHistory
import dev.byjtech.erp.document_management.domain.repository.DocumentEditHistoryRepository
import dev.byjtech.erp.document_management.dto.DocumentEditHistoryDTO
import dev.byjtech.erp.document_management.infrastructure.exposed.extensions.toDTO
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

class DocumentEditHistoryService(
    private val documentEditHistoryRepository: DocumentEditHistoryRepository
) {
    
    /**
     * Registra automáticamente los cambios entre dos versiones de un documento
     */
    fun recordDocumentChanges(
        oldDocument: Document,
        newDocument: Document,
        userId: UUID
    ) {
        val changes = detectChanges(oldDocument, newDocument)
        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        
        changes.forEach { change ->
            val history = DocumentEditHistory(
                id = UUID.randomUUID(),
                documentId = newDocument.id,
                fieldName = change.fieldName,
                oldValue = change.oldValue,
                newValue = change.newValue,
                userId = userId,
                createdAt = currentTime
            )
            
            documentEditHistoryRepository.save(history)
        }
    }
    
    /**
     * Obtiene el historial de edición de un documento
     */
    fun getDocumentHistory(documentId: UUID): List<DocumentEditHistoryDTO> {
        return documentEditHistoryRepository.findByDocumentId(documentId)
            .map { it.toDTO() }
    }
    
    /**
     * Obtiene todo el historial de ediciones (para la página principal)
     */
    fun getAllEditHistory(companyId: UUID): List<DocumentEditHistoryDTO> {
        return documentEditHistoryRepository.findByCompanyId(companyId)
            .map { it.toDTO() }
    }
    
    /**
     * Detecta cambios entre dos versiones de un documento
     */
    private fun detectChanges(oldDoc: Document, newDoc: Document): List<FieldChange> {
        val changes = mutableListOf<FieldChange>()
        
        // Comparar todos los campos auditables
        compareField("type", oldDoc.type.toString(), newDoc.type.toString(), changes)
        compareField("documentNumber", oldDoc.documentNumber, newDoc.documentNumber, changes)
        compareField("issueDate", oldDoc.issueDate.toString(), newDoc.issueDate.toString(), changes)
        compareField("dueDate", oldDoc.dueDate?.toString(), newDoc.dueDate?.toString(), changes)
        compareField("currency", oldDoc.currency, newDoc.currency, changes)
        compareField("netAmount", oldDoc.netAmount.toString(), newDoc.netAmount.toString(), changes)
        compareField("taxAmount", oldDoc.taxAmount.toString(), newDoc.taxAmount.toString(), changes)
        compareField("totalAmount", oldDoc.totalAmount.toString(), newDoc.totalAmount.toString(), changes)
        compareField("fileUrl", oldDoc.fileUrl, newDoc.fileUrl, changes)
        compareField("status", oldDoc.status.toString(), newDoc.status.toString(), changes)
        compareField("active", oldDoc.active.toString(), newDoc.active.toString(), changes)
        
        // No auditamos: id, createdBy, createdAt, updatedAt, companyId (no cambian)
        
        return changes
    }
    
    /**
     * Compara un campo específico y añade el cambio si es diferente
     */
    private fun compareField(
        fieldName: String,
        oldValue: String?,
        newValue: String?,
        changes: MutableList<FieldChange>
    ) {
        if (oldValue != newValue) {
            changes.add(
                FieldChange(
                    fieldName = fieldName,
                    oldValue = oldValue,
                    newValue = newValue
                )
            )
        }
    }
    
    /**
     * Clase auxiliar para representar un cambio de campo
     */
    private data class FieldChange(
        val fieldName: String,
        val oldValue: String?,
        val newValue: String?
    )
}