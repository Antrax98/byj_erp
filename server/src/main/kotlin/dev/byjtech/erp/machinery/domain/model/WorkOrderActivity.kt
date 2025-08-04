package dev.byjtech.erp.machinery.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

data class WorkOrderActivity(
    val id: UUID = UUID.randomUUID(),
    val workOrderId: UUID,
    val activityId: Int? = null, // Reference to MaintenanceActivity
    val name: String,
    val description: String? = null,
    val status: String, // PENDING, COMPLETED, SKIPPED, etc.
    val startDate: LocalDateTime? = null,
    val completionDate: LocalDateTime? = null,
    val comments: String? = null,
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val updatedAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
) {
    fun updateName(newName: String) = copy(name = newName)
    fun updateDescription(newDescription: String?) = copy(description = newDescription)
    fun updateStatus(newStatus: String) = copy(status = newStatus)
    fun start(startDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) = copy(startDate = startDate, status = "IN_PROGRESS")
    fun complete(completionDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) = copy(completionDate = completionDate, status = "COMPLETED")
    fun skip() = copy(status = "SKIPPED")
    fun addComments(newComments: String) = copy(comments = newComments)
}
