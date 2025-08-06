package dev.byjtech.erp.modules.machinery.response

import kotlinx.serialization.Serializable

@Serializable
data class MachineryHistoryResponse(
    val id: String,
    val machineryId: String,
    val userId: String,
    val field: String,
    val oldValue: String? = null,
    val newValue: String? = null,
    val createdAt: String,
    val comment: String? = null
)
