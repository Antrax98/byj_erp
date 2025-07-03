package dev.byjtech.erp.modules.document_management.domain.model

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class DocumentAuditLog(
    val id: Int,
    // val documentId: Int,
    val eventType: DocumentEventType,
    val description: String,
    // val userId: UUID,
    val createdAt: LocalDateTime
)