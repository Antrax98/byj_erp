package dev.byjtech.erp.core.domain.model


import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

data class Company(
    val id: UUID = UUID.randomUUID(),
    val rut: String,
    val name: String,
    val contactEmail: String,
    val createdAt: LocalDateTime? = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val updatedAt: LocalDateTime? = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val billing: Billing? = null
)