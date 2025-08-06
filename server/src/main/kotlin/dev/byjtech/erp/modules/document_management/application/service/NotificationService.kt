package dev.byjtech.erp.document_management.application.service

import dev.byjtech.erp.document_management.domain.model.*
import dev.byjtech.erp.document_management.domain.repository.*
import dev.byjtech.erp.document_management.infrastructure.service.EmailService
import dev.byjtech.erp.core.domain.repository.UserRepository
import kotlinx.datetime.*
import java.util.UUID

/**
 * Servicio para gestionar notificaciones de documentos
 */
class NotificationService(
    private val documentNotificationRepository: DocumentNotificationRepository,
    private val userNotificationSettingsRepository: UserNotificationSettingsRepository,
    private val documentRepository: DocumentRepository,
    private val userRepository: UserRepository
) {
    
    private val emailService = EmailService()

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

            // Procesar notificaciones PENDING existentes si el email está habilitado
            if (settings.emailEnabled) {
                println("DEBUG: Email habilitado para usuario ${settings.userId}, procesando notificaciones PENDING...")
                processPendingNotifications(settings.userId)
            } else {
                println("DEBUG: Email NO habilitado para usuario ${settings.userId}")
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

        // Calcular días hasta vencimiento
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val daysUntilExpiration = if (document.dueDate != null) {
            (document.dueDate.toEpochDays() - today.toEpochDays()).toInt()
        } else {
            0
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
        
        // Enviar email si está habilitado
        if (settings.emailEnabled) {
            val user = userRepository.find(settings.userId)
            if (user != null) {
                emailService.sendDocumentExpirationNotification(user, document, daysUntilExpiration)
            }
        }
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

        // Calcular días hasta vencimiento (será negativo para documentos vencidos)
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val daysUntilExpiration = if (document.dueDate != null) {
            (document.dueDate.toEpochDays() - today.toEpochDays()).toInt()
        } else {
            0
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
        
        // Enviar email si está habilitado
        if (settings.emailEnabled) {
            val user = userRepository.find(settings.userId)
            if (user != null) {
                emailService.sendDocumentExpirationNotification(user, document, daysUntilExpiration)
            }
        }
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

    /**
     * Procesa notificaciones PENDING existentes y envía emails si están habilitadas
     */
    private fun processPendingNotifications(userId: UUID) {
        println("DEBUG: Iniciando processPendingNotifications para usuario $userId")
        val pendingNotifications = documentNotificationRepository.findPendingByUserId(userId)
        val user = userRepository.find(userId)
        
        println("DEBUG: Encontradas ${pendingNotifications.size} notificaciones PENDING")
        
        if (user == null || pendingNotifications.isEmpty()) {
            println("DEBUG: Usuario es null: ${user == null}, notificaciones vacías: ${pendingNotifications.isEmpty()}")
            return
        }

        for (notification in pendingNotifications) {
            println("DEBUG: Procesando notificación ${notification.id}, sent_at: ${notification.sentAt}")
            // Solo procesar notificaciones que no han sido enviadas por email
            if (notification.sentAt == null) {
                val document = documentRepository.findById(notification.documentId)
                if (document != null) {
                    // Calcular días hasta vencimiento
                    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                    val daysUntilExpiration = if (document.dueDate != null) {
                        (document.dueDate.toEpochDays() - today.toEpochDays()).toInt()
                    } else {
                        0
                    }
                    
                    println("DEBUG: Enviando email para documento ${document.documentNumber}")
                    // Enviar email
                    emailService.sendDocumentExpirationNotification(user, document, daysUntilExpiration)
                    
                    // Marcar como enviado
                    println("DEBUG: Marcando notificación como enviada")
                    documentNotificationRepository.markAsSent(notification.id)
                } else {
                    println("DEBUG: Documento no encontrado para notificación ${notification.id}")
                }
            } else {
                println("DEBUG: Notificación ${notification.id} ya fue enviada")
            }
        }
    }
}
