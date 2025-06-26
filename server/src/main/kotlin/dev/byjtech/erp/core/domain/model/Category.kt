package dev.byjtech.erp.core.domain.model

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class Category (
    val id: UUID,
    val name: String,
    val description: String,
    val permissions: Set<Permission>?
)