package dev.byjtech.erp.modules.machinery.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

data class OperationalData(
    val id: UUID = UUID.randomUUID(),
    val machineryId: UUID,
    val type: String, // HOURS, KILOMETERS, etc.
    val value: Double,
    val recordedAt: LocalDateTime,
    val recordedBy: UUID,
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val updatedAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
) {
    fun updateValue(newValue: Double) = copy(value = newValue)
    fun updateRecordedAt(newRecordedAt: LocalDateTime) = copy(recordedAt = newRecordedAt)
}
