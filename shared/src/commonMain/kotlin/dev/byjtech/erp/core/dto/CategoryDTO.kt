package dev.byjtech.erp.core.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDTO(
    val id: String,
    val name: String,
    val description: String,
    val moduleId: String? // este es nullable
)
