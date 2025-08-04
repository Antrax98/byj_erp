package dev.byjtech.erp.machinery.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class MaintenanceActivity(
    val id: Int? = null,
    val scheduleId: java.util.UUID,
    val name: String,
    val description: String? = null,
    val estimatedDuration: Int? = null, // in minutes
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val updatedAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val isActive: Boolean = true
) {
    fun updateName(newName: String) = copy(name = newName)
    fun updateDescription(newDescription: String?) = copy(description = newDescription)
    fun updateEstimatedDuration(newDuration: Int?) = copy(estimatedDuration = newDuration)
    fun activate() = copy(isActive = true)
    fun deactivate() = copy(isActive = false)
}
