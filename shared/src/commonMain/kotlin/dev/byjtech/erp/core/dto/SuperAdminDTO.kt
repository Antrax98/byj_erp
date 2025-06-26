package dev.byjtech.erp.core.dto

import kotlinx.serialization.Serializable

@Serializable
data class SuperAdminDTO(
    val id: String,
    val userId: String,
    val description: String?
)