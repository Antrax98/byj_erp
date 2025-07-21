package dev.byjtech.erp.document_management.request

import kotlinx.serialization.Serializable

@Serializable
data class ChangeStatusRequest(
    val newStatus: String
)
