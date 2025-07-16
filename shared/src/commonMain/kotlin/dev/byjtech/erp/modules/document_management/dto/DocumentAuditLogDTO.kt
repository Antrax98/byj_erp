package dev.byjtech.erp.document_management.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class DocumentAuditLogDTO(
    val id: String,
    val documentId: String,
    val eventType: String,
    val description: String,
    val userId: String,
    val createdAt: LocalDateTime
)
