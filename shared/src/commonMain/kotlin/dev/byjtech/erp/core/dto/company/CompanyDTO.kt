package dev.byjtech.erp.core.dto.company

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CompanyDTO(
    val id: Int,
    val name: String,
    val contactEmail: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)