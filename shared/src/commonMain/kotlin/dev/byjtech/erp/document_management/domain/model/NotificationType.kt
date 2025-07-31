package dev.byjtech.erp.document_management.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class NotificationType {
    DOCUMENT_EXPIRING_SOON,
    DOCUMENT_EXPIRED,
    DOCUMENT_CREATED,
    DOCUMENT_UPDATED
}
