package dev.byjtech.erp.document_management.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserNotificationSettingsDTO(
    val id: String,
    val userId: String,
    val companyId: String,
    val daysBeforeExpiration: Int,
    val emailEnabled: Boolean,
    val systemEnabled: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
