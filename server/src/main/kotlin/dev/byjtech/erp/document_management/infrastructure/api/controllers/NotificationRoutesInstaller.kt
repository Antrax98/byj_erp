package dev.byjtech.erp.document_management.infrastructure.api.controllers

import dev.byjtech.erp.document_management.DocumentManagementDefinition
import dev.byjtech.erp.document_management.application.service.NotificationService
import dev.byjtech.erp.document_management.infrastructure.auth.DocumentManagementAuthWrapper
import dev.byjtech.erp.document_management.infrastructure.exposed.extensions.toDTO
import dev.byjtech.erp.document_management.request.UpdateNotificationSettingsRequest
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import java.util.UUID

class NotificationRoutesInstaller(
    private val notificationService: NotificationService,
    private val authWrapper: DocumentManagementAuthWrapper
) : RoutesInstaller {
    
    override fun Route.installRoutes() {
        route("/notifications") {
            notificationRoutes(notificationService, authWrapper)
        }
    }
}

fun Route.notificationRoutes(
    notificationService: NotificationService, 
    authWrapper: DocumentManagementAuthWrapper
) {
    
    get("/pending") {
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Notifications.View.key
            )
        )
        
        val notifications = notificationService.getPendingNotifications(session.userId)
        call.respond(HttpStatusCode.OK, notifications.map { it.toDTO() })
    }
    
    get("/all") {
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Notifications.View.key
            )
        )
        
        val notifications = notificationService.getAllNotifications(session.userId)
        call.respond(HttpStatusCode.OK, notifications.map { it.toDTO() })
    }
    
    get("/count") {
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Notifications.View.key
            )
        )
        
        val count = notificationService.countPendingNotifications(session.userId)
        call.respond(HttpStatusCode.OK, mapOf("count" to count))
    }
    
    put("/{id}/read") {
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Notifications.View.key
            )
        )
        
        val notificationId = call.parameters["id"]?.let { UUID.fromString(it) }
            ?: return@put call.respond(HttpStatusCode.BadRequest, "ID de notificación inválido")
        
        val updatedNotification = notificationService.markAsRead(notificationId)
        if (updatedNotification != null) {
            call.respond(HttpStatusCode.OK, updatedNotification.toDTO())
        } else {
            call.respond(HttpStatusCode.NotFound, "Notificación no encontrada")
        }
    }
    
    get("/settings") {
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Notifications.Configure.key
            )
        )
        
        val settings = notificationService.getOrCreateUserSettings(
            session.userId, 
            session.companyId ?: throw IllegalStateException("Usuario sin compañía asignada")
        )
        call.respond(HttpStatusCode.OK, settings.toDTO())
    }
    
    put("/settings") {
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Notifications.Configure.key
            )
        )
        
        val request = call.receive<UpdateNotificationSettingsRequest>()
        
        if (request.daysBeforeExpiration < 1 || request.daysBeforeExpiration > 365) {
            return@put call.respond(
                HttpStatusCode.BadRequest, 
                "Los días de anticipación deben estar entre 1 y 365"
            )
        }
        
        val currentSettings = notificationService.getOrCreateUserSettings(
            session.userId, 
            session.companyId ?: throw IllegalStateException("Usuario sin compañía asignada")
        )
        val updatedSettings = currentSettings.copy(
            daysBeforeExpiration = request.daysBeforeExpiration,
            emailEnabled = request.emailEnabled,
            systemEnabled = request.systemEnabled
        )
        
        val result = notificationService.updateUserSettings(updatedSettings)
        call.respond(HttpStatusCode.OK, result.toDTO())
    }
}
