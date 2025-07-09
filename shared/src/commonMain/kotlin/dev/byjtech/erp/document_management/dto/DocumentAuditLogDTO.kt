package dev.byjtech.erp.document_management.dto

import kotlinx.datetime.LocalDateTime

data class DocumentAuditLogDTO(
    val id: String,
    val documentId: String,
    val eventType: String,
    val description: String,
    val userId: String,
    val createdAt: LocalDateTime
)
