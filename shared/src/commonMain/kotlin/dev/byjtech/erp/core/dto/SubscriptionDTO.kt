package dev.byjtech.erp.core.dto

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionDTO(
    val id: String,
    val companyId: String,
    val moduleId: String,
    val isActive: Boolean,
    val isAccessible: Boolean
)