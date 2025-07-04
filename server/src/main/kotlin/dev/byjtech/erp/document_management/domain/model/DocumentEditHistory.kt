package dev.byjtech.erp.document_management.domain.model


import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class DocumentEditHistory(
    val id: Int,
    // val documentId: Int,
    val fieldName: String,
    val oldValue: String?,
    val newValue: String?,
    // val userId: UUID,
    val createdAt: LocalDateTime
)