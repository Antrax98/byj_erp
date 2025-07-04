package dev.byjtech.erp.document_management.domain.model

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class DocumentAuditLog(
    val id: UUID,
    val documentId: UUID,
    val eventType: DocumentEventType,
    val description: String,
    val userId: UUID,
    val createdAt: LocalDateTime
)