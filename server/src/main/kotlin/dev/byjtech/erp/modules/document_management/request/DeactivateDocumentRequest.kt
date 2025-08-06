package dev.byjtech.erp.document_management.request

import kotlinx.serialization.Serializable

@Serializable
data class DeactivateDocumentRequest(
    val confirm: Boolean = false,
    val reason: String? = null
)
