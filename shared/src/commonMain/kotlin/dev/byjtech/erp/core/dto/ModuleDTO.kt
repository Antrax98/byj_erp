package dev.byjtech.erp.core.dto


import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ModuleDTO(
    val id: String,
    val name: String,
    val displayName: String,
    val description: String,
    val developerOnly: Boolean
)