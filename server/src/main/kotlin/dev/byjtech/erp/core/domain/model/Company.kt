package dev.byjtech.erp.core.domain.model


import kotlinx.datetime.LocalDateTime

data class Company(
    val id: Int,
    val name: String,
    val contactEmail: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)