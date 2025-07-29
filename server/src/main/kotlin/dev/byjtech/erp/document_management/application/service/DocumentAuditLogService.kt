package dev.byjtech.erp.document_management.application.service

import dev.byjtech.erp.document_management.domain.model.DocumentAuditLog
import dev.byjtech.erp.document_management.domain.model.DocumentEventType
import dev.byjtech.erp.document_management.domain.repository.DocumentAuditLogRepository
import dev.byjtech.erp.document_management.dto.DocumentAuditLogDTO
import dev.byjtech.erp.document_management.infrastructure.exposed.extensions.toDTO
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

class DocumentAuditLogService(
    private val documentAuditLogRepository: DocumentAuditLogRepository
) {
    
    fun logDocumentDeactivated(documentId: UUID, userId: UUID, reason: String? = null): DocumentAuditLogDTO {
        val description = buildString {
            append("Documento desactivado")
            if (!reason.isNullOrBlank()) {
                append(". Razón: $reason")
            }
        }
        
        val auditLog = DocumentAuditLog(
            id = UUID.randomUUID(),
            documentId = documentId,
            eventType = DocumentEventType.DEACTIVATED,
            description = description,
            userId = userId,
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
        
        val savedLog = documentAuditLogRepository.create(auditLog)
        return savedLog.toDTO()
    }
    
    fun logDocumentReactivated(documentId: UUID, userId: UUID, reason: String? = null): DocumentAuditLogDTO {
        val description = buildString {
            append("Documento reactivado")
            if (!reason.isNullOrBlank()) {
                append(". Razón: $reason")
            }
        }
        
        val auditLog = DocumentAuditLog(
            id = UUID.randomUUID(),
            documentId = documentId,
            eventType = DocumentEventType.REACTIVATED,
            description = description,
            userId = userId,
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
        
        val savedLog = documentAuditLogRepository.create(auditLog)
        return savedLog.toDTO()
    }
    
    fun getDocumentAuditLogs(documentId: UUID): List<DocumentAuditLogDTO> {
        return documentAuditLogRepository.findByDocumentId(documentId)
            .map { it.toDTO() }
    }
}