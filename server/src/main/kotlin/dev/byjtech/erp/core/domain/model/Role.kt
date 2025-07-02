package dev.byjtech.erp.core.domain.model

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class Role(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val permissions: Set<Permission>? = setOf(),
    val companyId: UUID
)
