package dev.byjtech.erp.machinery.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

data class MachineryNotification(
    val id: UUID = UUID.randomUUID(),
    val machineryId: UUID? = null,
    val documentId: UUID? = null,
    val scheduleId: UUID? = null,
    val type: String, // DOCUMENT_EXPIRATION, MAINTENANCE_DUE, etc.
    val title: String,
    val message: String,
    val priority: String, // LOW, MEDIUM, HIGH
    val status: String, // PENDING, READ, ARCHIVED
    val dueDate: LocalDateTime? = null,
    val notifyUsers: String, // JSON array of user IDs to notify
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val updatedAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
) {
    fun updateTitle(newTitle: String) = copy(title = newTitle)
    fun updateMessage(newMessage: String) = copy(message = newMessage)
    fun updatePriority(newPriority: String) = copy(priority = newPriority)
    fun markAsRead() = copy(status = "READ")
    fun archive() = copy(status = "ARCHIVED")
    fun updateDueDate(newDueDate: LocalDateTime?) = copy(dueDate = newDueDate)
    fun updateNotifyUsers(newNotifyUsers: String) = copy(notifyUsers = newNotifyUsers)
    
    fun isOverdue(): Boolean {
        return dueDate?.let { 
            it < Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        } ?: false
    }
}
