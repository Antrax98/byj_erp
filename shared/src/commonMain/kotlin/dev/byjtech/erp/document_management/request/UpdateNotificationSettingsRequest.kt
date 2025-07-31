package dev.byjtech.erp.document_management.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateNotificationSettingsRequest(
    val daysBeforeExpiration: Int,
    val emailEnabled: Boolean,
    val systemEnabled: Boolean
)
