package dev.byjtech.erp.core.domain.model

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class Module (
    val id: UUID,
    val name: String,
    val displayName: String,
    val description: String,
    val developerOnly: Boolean,
    val categories: Set<Category>?
)