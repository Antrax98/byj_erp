package dev.byjtech.erp.document_management.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class NotificationStatus {
    PENDING,
    SENT,
    READ,
    DISMISSED
}
