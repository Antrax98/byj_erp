package dev.byjtech.erp.modules.machinery.infrastructure.exposed.repository

import dev.byjtech.erp.modules.machinery.domain.model.MachineryNotification
import dev.byjtech.erp.modules.machinery.domain.repository.MachineryNotificationRepository
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MachineryDocumentEntity
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MachineryEntity
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MachineryNotificationEntity
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MaintenanceScheduleEntity
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions.fromModel
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryNotificationsTable
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class MachineryNotificationRepositoryImpl(private val db: Database) : MachineryNotificationRepository {
    
    override fun save(notification: MachineryNotification): MachineryNotification {
        return transaction(db) {
            val machinery = notification.machineryId?.let { 
                MachineryEntity.findById(it)
                    ?: throw IllegalArgumentException("Machinery with id $it not found")
            }
            
            val document = notification.documentId?.let { 
                MachineryDocumentEntity.findById(it)
                    ?: throw IllegalArgumentException("MachineryDocument with id $it not found")
            }
            
            val schedule = notification.scheduleId?.let { 
                MaintenanceScheduleEntity.findById(it)
                    ?: throw IllegalArgumentException("MaintenanceSchedule with id $it not found")
            }
            
            val entity = MachineryNotificationEntity.new {
                this.machinery = machinery
                this.document = document
                this.schedule = schedule
                type = notification.type
                title = notification.title
                message = notification.message
                priority = notification.priority
                status = notification.status
                dueDate = notification.dueDate?.toJavaLocalDateTime()
                notifyUsers = notification.notifyUsers
                createdAt = java.time.LocalDateTime.now()
                updatedAt = java.time.LocalDateTime.now()
            }
            entity.toModel()
        }
    }

    override fun findById(id: UUID): MachineryNotification? {
        return transaction(db) {
            MachineryNotificationEntity.findById(id)?.toModel()
        }
    }

    override fun findByMachineryId(machineryId: UUID): List<MachineryNotification> {
        return transaction(db) {
            MachineryNotificationEntity.find { MachineryNotificationsTable.machineryId eq machineryId }
                .orderBy(MachineryNotificationsTable.createdAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun findByDocumentId(documentId: UUID): List<MachineryNotification> {
        return transaction(db) {
            MachineryNotificationEntity.find { MachineryNotificationsTable.documentId eq documentId }
                .orderBy(MachineryNotificationsTable.createdAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun findByScheduleId(scheduleId: UUID): List<MachineryNotification> {
        return transaction(db) {
            MachineryNotificationEntity.find { MachineryNotificationsTable.scheduleId eq scheduleId }
                .orderBy(MachineryNotificationsTable.createdAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun findByType(type: String): List<MachineryNotification> {
        return transaction(db) {
            MachineryNotificationEntity.find { MachineryNotificationsTable.type eq type }
                .orderBy(MachineryNotificationsTable.createdAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun findByStatus(status: String): List<MachineryNotification> {
        return transaction(db) {
            MachineryNotificationEntity.find { MachineryNotificationsTable.status eq status }
                .orderBy(MachineryNotificationsTable.createdAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun findByPriority(priority: String): List<MachineryNotification> {
        return transaction(db) {
            MachineryNotificationEntity.find { MachineryNotificationsTable.priority eq priority }
                .orderBy(MachineryNotificationsTable.createdAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun findAll(): List<MachineryNotification> {
        return transaction(db) {
            MachineryNotificationEntity.all()
                .orderBy(MachineryNotificationsTable.createdAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun update(notification: MachineryNotification): MachineryNotification {
        return transaction(db) {
            val entity = MachineryNotificationEntity.findById(notification.id)
                ?: throw IllegalArgumentException("MachineryNotification with id ${notification.id} not found")
            entity.fromModel(notification)
            entity.updatedAt = java.time.LocalDateTime.now()
            entity.toModel()
        }
    }

    override fun delete(id: UUID) {
        transaction(db) {
            MachineryNotificationEntity.findById(id)?.delete()
        }
    }

    override fun findPendingNotifications(): List<MachineryNotification> {
        return transaction(db) {
            MachineryNotificationEntity.find { MachineryNotificationsTable.status eq "PENDING" }
                .orderBy(MachineryNotificationsTable.createdAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun findOverdueNotifications(currentDate: LocalDateTime): List<MachineryNotification> {
        return transaction(db) {
            MachineryNotificationEntity.find { 
                (MachineryNotificationsTable.dueDate less currentDate.toJavaLocalDateTime()) and
                (MachineryNotificationsTable.status neq "ARCHIVED")
            }.orderBy(MachineryNotificationsTable.createdAt to SortOrder.DESC)
                .map { it.toModel() }
        }
    }

    override fun findByUserToNotify(userId: UUID): List<MachineryNotification> {
        return transaction(db) {
            // Búsqueda simple sin LIKE para evitar problemas de compilación
            MachineryNotificationEntity.all()
                .filter { it.notifyUsers.contains(userId.toString()) }
                .sortedByDescending { it.createdAt }
                .map { it.toModel() }
        }
    }
}
