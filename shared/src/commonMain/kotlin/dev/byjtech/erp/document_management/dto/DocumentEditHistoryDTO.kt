package dev.byjtech.erp.document_management.dto

import kotlinx.datetime.LocalDateTime

data class DocumentEditHistoryDTO(
    val id: String,
    val documentId: String,
    val fieldName: String,
    val oldValue: String?,
    val newValue: String?,
    val userId: String,
    val createdAt: LocalDateTime
)
