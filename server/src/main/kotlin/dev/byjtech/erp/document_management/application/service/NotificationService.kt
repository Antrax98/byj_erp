package dev.byjtech.erp.document_management.application.service

import dev.byjtech.erp.document_management.domain.model.*
import dev.byjtech.erp.document_management.domain.repository.*
import kotlinx.datetime.*
import java.util.UUID

/**
 * Servicio para gestionar notificaciones de documentos
 */
class NotificationService(
    private val documentNotificationRepository: DocumentNotificationRepository,
    private val userNotificationSettingsRepository: UserNotificationSettingsRepository,
    private val documentRepository: DocumentRepository
) {

    /**
     * Verifica documentos próximos a vencer y genera notificaciones
     */
    fun checkExpiringDocuments(companyId: UUID) {
        // Obtener todas las configuraciones de notificación de la compañía
        val allSettings = userNotificationSettingsRepository.findByCompanyId(companyId)
        
        if (allSettings.isEmpty()) {
            return
        }

        // Obtener todos los documentos de la compañía
        val documents = documentRepository.findByCompanyId(companyId)
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        for (settings in allSettings) {
            if (!settings.systemEnabled) {
                continue
            }

            for (document in documents) {
                if (document.dueDate == null) {
                    continue
                }
                
                val daysUntilExpiration = (document.dueDate.toEpochDays() - today.toEpochDays()).toInt()
                
                // Si el documento vence dentro del período configurado (incluye hoy y días anteriores)
                if (daysUntilExpiration <= settings.daysBeforeExpiration && daysUntilExpiration >= 0) {
                    createExpirationNotification(document, settings)
                }
                
                // Si el documento ya venció y no se ha notificado
                if (daysUntilExpiration < 0) {
                    createExpiredNotification(document, settings)
                }
            }
        }
    }

    /**
     * Crea una notificación de documento próximo a vencer
     */
    private fun createExpirationNotification(document: Document, settings: UserNotificationSettings) {
        // Verificar si ya existe una notificación pendiente
        val existingNotification = documentNotificationRepository.findExistingNotification(
            documentId = document.id,
            userId = settings.userId,
            notificationType = NotificationType.DOCUMENT_EXPIRING_SOON
        )
        
        if (existingNotification != null) {
            return
        }

        val notification = DocumentNotification(
            id = UUID.randomUUID(),
            documentId = document.id,
            userId = settings.userId,
            notificationType = NotificationType.DOCUMENT_EXPIRING_SOON,
            title = "Documento próximo a vencer",
            message = "El documento ${document.documentNumber} vence en ${settings.daysBeforeExpiration} días (${document.dueDate})",
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )

        documentNotificationRepository.save(notification)
    }

    /**
     * Crea una notificación de documento vencido
     */
    private fun createExpiredNotification(document: Document, settings: UserNotificationSettings) {
        // Verificar si ya existe una notificación pendiente
        val existingNotification = documentNotificationRepository.findExistingNotification(
            documentId = document.id,
            userId = settings.userId,
            notificationType = NotificationType.DOCUMENT_EXPIRED
        )
        
        if (existingNotification != null) {
            return
        }

        val notification = DocumentNotification(
            id = UUID.randomUUID(),
            documentId = document.id,
            userId = settings.userId,
            notificationType = NotificationType.DOCUMENT_EXPIRED,
            title = "Documento vencido",
            message = "El documento ${document.documentNumber} venció el ${document.dueDate}",
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )

        documentNotificationRepository.save(notification)
    }

    /**
     * Obtiene las notificaciones pendientes de un usuario
     */
    fun getPendingNotifications(userId: UUID): List<DocumentNotification> {
        return documentNotificationRepository.findPendingByUserId(userId)
    }

    /**
     * Obtiene todas las notificaciones de un usuario
     */
    fun getAllNotifications(userId: UUID): List<DocumentNotification> {
        return documentNotificationRepository.findByUserId(userId)
    }

    /**
     * Marca una notificación como leída
     */
    fun markAsRead(notificationId: UUID): DocumentNotification? {
        return documentNotificationRepository.updateStatus(notificationId, NotificationStatus.READ)
    }

    /**
     * Cuenta las notificaciones pendientes de un usuario
     */
    fun countPendingNotifications(userId: UUID): Int {
        return documentNotificationRepository.countPendingByUserId(userId)
    }

    /**
     * Obtiene o crea configuraciones de notificación para un usuario
     */
    fun getOrCreateUserSettings(userId: UUID, companyId: UUID): UserNotificationSettings {
        return userNotificationSettingsRepository.findByUserId(userId)
            ?: userNotificationSettingsRepository.createDefaultSettings(userId, companyId)
    }

    /**
     * Actualiza las configuraciones de notificación de un usuario
     */
    fun updateUserSettings(settings: UserNotificationSettings): UserNotificationSettings {
        val updatedSettings = settings.copy(
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
        return userNotificationSettingsRepository.save(updatedSettings)
    }
}
