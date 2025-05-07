package dev.byjtech.erp.core.dto.module


import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ModuleDTO(
    val id: Int,
    val name: String,
    val displayName: String,
    val description: String,
    val version: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val developerOnly: Boolean
)