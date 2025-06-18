package dev.byjtech.erp.core.domain.model

import java.util.UUID

data class SuperAdmin(
    val id: UUID,
    val userId: UUID,
    val description: String?
)
