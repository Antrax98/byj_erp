package dev.byjtech.erp.core.dto

import kotlinx.serialization.Serializable

@Serializable
data class PermissionDTO(
    val id: String,
    val name: String,
    val description: String
)