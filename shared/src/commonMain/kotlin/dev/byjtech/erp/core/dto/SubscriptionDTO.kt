package dev.byjtech.erp.core.dto

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionDTO(
    val id: Int,
    val companyId: Int,
    val moduleId: Int,
    val isActive: Boolean,
    val isAccessible: Boolean
)