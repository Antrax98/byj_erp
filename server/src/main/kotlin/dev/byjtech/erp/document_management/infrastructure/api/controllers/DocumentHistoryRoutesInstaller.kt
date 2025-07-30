package dev.byjtech.erp.document_management.infrastructure.api.controllers

import dev.byjtech.erp.document_management.DocumentManagementDefinition
import dev.byjtech.erp.document_management.application.service.DocumentEditHistoryService
import dev.byjtech.erp.document_management.application.service.DocumentAuditLogService
import dev.byjtech.erp.document_management.infrastructure.auth.DocumentManagementAuthWrapper
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*

class DocumentHistoryRoutesInstaller(
    private val documentEditHistoryService: DocumentEditHistoryService,
    private val documentAuditLogService: DocumentAuditLogService,
    private val authWrapper: DocumentManagementAuthWrapper
) : RoutesInstaller {
    
    override fun Route.installRoutes() {
        // Rutas para historial de ediciones
        route("/edit-history") {
            editHistoryRoutes(documentEditHistoryService, authWrapper)
        }
        
        // Rutas para logs de auditoría
        route("/audit-logs") {
            auditLogRoutes(documentAuditLogService, authWrapper)
        }
    }
}

fun Route.editHistoryRoutes(
    documentEditHistoryService: DocumentEditHistoryService,
    authWrapper: DocumentManagementAuthWrapper
) {
    get("/all") {
        try {
            // 1. Validar sesión y autorización
            val session = authWrapper.authorizeOrThrow(
                call,
                requiredAnyPermissions = setOf(
                    DocumentManagementDefinition.Documents.View.key
                )
            )
            
            // 2. Obtener ID de la compañía del usuario autenticado
            val companyId = session.companyId ?: throw IllegalStateException("User has no company")
            
            // 3. Obtener todo el historial de edición de la compañía
            val editHistory = documentEditHistoryService.getAllEditHistory(companyId)
            
            call.respond(HttpStatusCode.OK, editHistory)
            
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error retrieving edit history: ${e.message}")
        }
    }
}

fun Route.auditLogRoutes(
    documentAuditLogService: DocumentAuditLogService,
    authWrapper: DocumentManagementAuthWrapper
) {
    get("/all") {
        try {
            // 1. Validar sesión y autorización
            val session = authWrapper.authorizeOrThrow(
                call,
                requiredAnyPermissions = setOf(
                    DocumentManagementDefinition.Documents.View.key
                )
            )
            
            // 2. Obtener ID de la compañía del usuario autenticado
            val companyId = session.companyId ?: throw IllegalStateException("User has no company")
            
            // 3. Obtener todos los logs de auditoría de la compañía
            val auditLogs = documentAuditLogService.getAllAuditLogs(companyId)
            
            call.respond(HttpStatusCode.OK, auditLogs)
            
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error retrieving audit logs: ${e.message}")
        }
    }
}
