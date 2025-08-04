package dev.byjtech.erp.machinery.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

data class MaintenanceSchedule(
    val id: UUID = UUID.randomUUID(),
    val machineryId: UUID,
    val name: String,
    val description: String? = null,
    val intervalType: String, // TIME, HOURS, KILOMETERS
    val intervalValue: Int,
    val lastMaintenanceDate: LocalDateTime? = null,
    val nextMaintenanceDate: LocalDateTime? = null,
    val lastOperationalValue: Double? = null,
    val nextOperationalValue: Double? = null,
    val createdBy: UUID,
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val updatedAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val isActive: Boolean = true
) {
    fun updateName(newName: String) = copy(name = newName)
    fun updateDescription(newDescription: String?) = copy(description = newDescription)
    fun updateInterval(newIntervalType: String, newIntervalValue: Int) = copy(intervalType = newIntervalType, intervalValue = newIntervalValue)
    fun updateLastMaintenance(date: LocalDateTime, operationalValue: Double?) = copy(lastMaintenanceDate = date, lastOperationalValue = operationalValue)
    fun updateNextMaintenance(date: LocalDateTime, operationalValue: Double?) = copy(nextMaintenanceDate = date, nextOperationalValue = operationalValue)
    fun activate() = copy(isActive = true)
    fun deactivate() = copy(isActive = false)
}
