package dev.byjtech.erp.core.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CompanyDTO(
    val id: String,
    val name: String,
    val rut: String,
    val contactEmail: String,
    val billingId: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)