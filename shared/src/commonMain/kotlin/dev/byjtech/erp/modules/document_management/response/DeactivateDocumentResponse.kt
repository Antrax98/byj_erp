package dev.byjtech.erp.document_management.response

import dev.byjtech.erp.document_management.dto.DocumentDTO
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class DeactivateDocumentResponse(
    val document: DocumentDTO,
    val message: String,
    val deactivatedAt: LocalDateTime
)
