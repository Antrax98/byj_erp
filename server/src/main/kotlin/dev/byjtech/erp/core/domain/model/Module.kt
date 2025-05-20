package dev.byjtech.erp.core.domain.model

import kotlinx.datetime.LocalDateTime

data class Module (
    val id: Int,
    val name: String,
    val displayName: String,
    val description: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val developerOnly: Boolean,
    val categories: Set<Category>?
)