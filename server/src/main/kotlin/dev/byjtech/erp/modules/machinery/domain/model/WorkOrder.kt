package dev.byjtech.erp.modules.machinery.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

data class WorkOrder(
    val id: UUID = UUID.randomUUID(),
    val machineryId: UUID,
    val scheduleId: UUID? = null,
    val orderNumber: String,
    val description: String,
    val type: String, // PREVENTIVE, CORRECTIVE, etc.
    val status: String, // PENDING, IN_PROGRESS, COMPLETED, etc.
    val scheduledDate: LocalDateTime,
    val startDate: LocalDateTime? = null,
    val completionDate: LocalDateTime? = null,
    val assignedTo: UUID? = null,
    val createdBy: UUID,
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val updatedAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val comments: String? = null
) {
    fun updateDescription(newDescription: String) = copy(description = newDescription)
    fun updateStatus(newStatus: String) = copy(status = newStatus)
    fun updateScheduledDate(newScheduledDate: LocalDateTime) = copy(scheduledDate = newScheduledDate)
    fun start(startDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) = copy(startDate = startDate, status = "IN_PROGRESS")
    fun complete(completionDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) = copy(completionDate = completionDate, status = "COMPLETED")
    fun assignTo(userId: UUID) = copy(assignedTo = userId)
    fun addComments(newComments: String) = copy(comments = newComments)
}
