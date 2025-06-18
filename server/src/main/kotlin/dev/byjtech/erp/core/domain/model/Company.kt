package dev.byjtech.erp.core.domain.model


import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class Company(
    val id: UUID,
    val name: String,
    val contactEmail: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)