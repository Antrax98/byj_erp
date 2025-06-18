package dev.byjtech.erp.core.domain.model

import java.util.UUID

data class Permission (
    val id: UUID,
    val name: String,
    val description: String
)