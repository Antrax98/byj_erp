package dev.byjtech.erp.modules.machinery.domain.repository

import dev.byjtech.erp.modules.machinery.domain.model.MachineryNotification
import kotlinx.datetime.LocalDateTime
import java.util.UUID

interface MachineryNotificationRepository {
    fun save(notification: MachineryNotification): MachineryNotification
    fun findById(id: UUID): MachineryNotification?
    fun findByMachineryId(machineryId: UUID): List<MachineryNotification>
    fun findByDocumentId(documentId: UUID): List<MachineryNotification>
    fun findByScheduleId(scheduleId: UUID): List<MachineryNotification>
    fun findByType(type: String): List<MachineryNotification>
    fun findByStatus(status: String): List<MachineryNotification>
    fun findByPriority(priority: String): List<MachineryNotification>
    fun findAll(): List<MachineryNotification>
    fun update(notification: MachineryNotification): MachineryNotification
    fun delete(id: UUID)
    fun findPendingNotifications(): List<MachineryNotification>
    fun findOverdueNotifications(currentDate: LocalDateTime): List<MachineryNotification>
    fun findByUserToNotify(userId: UUID): List<MachineryNotification>
}
