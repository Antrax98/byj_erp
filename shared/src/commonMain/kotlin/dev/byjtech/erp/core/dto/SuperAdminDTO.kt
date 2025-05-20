package dev.byjtech.erp.core.dto

import kotlinx.serialization.Serializable

@Serializable
data class SuperAdminDTO(
    val id: Int,
    val userId: Int,
    val description: String?
)