package dev.byjtech.erp.core.domain.model

import kotlinx.datetime.LocalDateTime

data class Permission (
    val id: Int,
    val name: String,
    val description: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)