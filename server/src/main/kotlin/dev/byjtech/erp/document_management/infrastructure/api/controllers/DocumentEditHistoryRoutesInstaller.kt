package dev.byjtech.erp.document_management.infrastructure.api.controllers

import dev.byjtech.erp.document_management.DocumentManagementDefinition
import dev.byjtech.erp.document_management.application.service.DocumentEditHistoryService
import dev.byjtech.erp.document_management.infrastructure.auth.DocumentManagementAuthWrapper
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import java.util.UUID

class DocumentEditHistoryRoutesInstaller(
    private val documentEditHistoryService: DocumentEditHistoryService,
    private val authWrapper: DocumentManagementAuthWrapper
) : RoutesInstaller {
    
    override fun Route.installRoutes() {
        route("/documents/{documentId}/edit-history") {
            documentEditHistoryRoutes(documentEditHistoryService, authWrapper)
        }
    }
}

fun Route.documentEditHistoryRoutes(
    documentEditHistoryService: DocumentEditHistoryService,
    authWrapper: DocumentManagementAuthWrapper
) {
    
    get {
        try {
            // 1. Validar sesión y autorización
            val session = authWrapper.authorizeOrThrow(
                call,
                requiredAnyPermissions = setOf(
                    DocumentManagementDefinition.Documents.View.key
                )
            )
            
            // 2. Obtener ID del documento
            val documentIdParam = call.parameters["documentId"]
            if (documentIdParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Document ID is required")
                return@get
            }
            
            val documentId = try {
                UUID.fromString(documentIdParam)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid document ID format")
                return@get
            }
            
            // 3. Obtener historial de edición
            val history = documentEditHistoryService.getDocumentHistory(documentId)
            
            call.respond(HttpStatusCode.OK, history)
            
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Validation error")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error retrieving document edit history: ${e.message}")
        }
    }
    
    get("/summary") {
        try {
            // 1. Validar sesión y autorización
            val session = authWrapper.authorizeOrThrow(
                call,
                requiredAnyPermissions = setOf(
                    DocumentManagementDefinition.Documents.View.key
                )
            )
            
            // 2. Obtener ID del documento
            val documentIdParam = call.parameters["documentId"]
            if (documentIdParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Document ID is required")
                return@get
            }
            
            val documentId = try {
                UUID.fromString(documentIdParam)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid document ID format")
                return@get
            }
            
            // 3. Obtener historial y crear resumen
            val history = documentEditHistoryService.getDocumentHistory(documentId)
            
            val summary = mapOf(
                "totalChanges" to history.size,
                "lastModified" to history.maxByOrNull { it.createdAt }?.createdAt,
                "modifiedFields" to history.map { it.fieldName }.distinct(),
                "uniqueEditors" to history.map { it.userId }.distinct().size
            )
            
            call.respond(HttpStatusCode.OK, summary)
            
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Validation error")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error retrieving document edit history summary: ${e.message}")
        }
    }
}
