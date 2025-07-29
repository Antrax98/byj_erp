package dev.byjtech.erp.document_management.infrastructure.api.controllers

import dev.byjtech.erp.document_management.DocumentManagementDefinition
import dev.byjtech.erp.document_management.domain.repository.DocumentRepository
import dev.byjtech.erp.document_management.application.service.DocumentService
import dev.byjtech.erp.document_management.infrastructure.auth.DocumentManagementAuthWrapper
import dev.byjtech.erp.document_management.infrastructure.exposed.extensions.toDTO
import dev.byjtech.erp.document_management.infrastructure.exposed.extensions.toDomain
import dev.byjtech.erp.document_management.infrastructure.exposed.extensions.applyUpdate
import dev.byjtech.erp.document_management.request.CreateDocumentRequest
import dev.byjtech.erp.document_management.request.UpdateDocumentRequest
import dev.byjtech.erp.document_management.request.DeactivateDocumentRequest
import dev.byjtech.erp.document_management.response.DeactivateDocumentResponse
import dev.byjtech.erp.modules.document_management.request.DocumentSearchRequest
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.request.receive
import io.ktor.server.request.receiveText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.datetime.*
import java.util.UUID

class DocumentRoutesInstaller(
    private val documentService: DocumentService,
    private val authWrapper: DocumentManagementAuthWrapper
) : RoutesInstaller {
    
    override fun Route.installRoutes() {
        route("/documents") {
            documentsRoutes(documentService, authWrapper)
        }
    }
}

fun Route.documentsRoutes(
    documentService: DocumentService, 
    authWrapper: DocumentManagementAuthWrapper
) {
    
    get("/all") {
        // 1. Validar sesión y autorización
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.View.key
            )
        )
        
        // 2. Verificar contexto de empresa
        if (session.companyId == null) {
            call.respond(HttpStatusCode.BadRequest, "User has no company")
            return@get
        }
        
        val companyId = session.companyId // Ya sabemos que no es null por la validación anterior
        
        // 3. Usar service para obtener documentos por compañía
        try {
            val documentsDTO = documentService.getAllDocumentsByCompany(companyId)
            call.respond(HttpStatusCode.OK, documentsDTO)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "Error retrieving documents: ${e.message}")
        }
    }
    
    post("/search") {
        // 1. Validar sesión y autorización
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.View.key
            )
        )
        
        // 2. Verificar contexto de empresa
        if (session.companyId == null) {
            call.respond(HttpStatusCode.BadRequest, "User has no company")
            return@post
        }
        
        // 3. Recibir parámetros de búsqueda
        val searchRequest = try {
            call.receive<DocumentSearchRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid search request format")
            return@post
        }
        
        // 4. Realizar búsqueda
        try {
            val searchResponse = documentService.searchDocuments(session.companyId, searchRequest)
            call.respond(HttpStatusCode.OK, searchResponse)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error performing search: ${e.message}")
        }
    }
    
    get("/{documentId}") {
        // 1. Validar sesión y autorización
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.View.key
            )
        )
        
        // 2. Extraer y validar parámetros
        val documentIdString = call.parameters["documentId"]
        if (documentIdString == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing document ID")
            return@get
        }
        
        val documentId = try {
            UUID.fromString(documentIdString)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, "Invalid UUID format for document ID")
            return@get
        }
        
        // 3. Verificar contexto de empresa
        if (session.companyId == null) {
            call.respond(HttpStatusCode.BadRequest, "User has no company")
            return@get
        }
        
        val companyId = session.companyId // Ya sabemos que no es null por la validación anterior
        
        // 4. Usar service para obtener documento (con lógica de negocio)
        try {
            val documentDTO = documentService.getDocumentById(documentId, companyId)
            if (documentDTO != null) {
                call.respond(HttpStatusCode.OK, documentDTO)
            } else {
                call.respond(HttpStatusCode.NotFound, "Document not found")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error retrieving document: ${e.message}")
        }
    }
    
    post {
        // 1. Validar sesión y autorización
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.Create.key
            )
        )
        
        // 2. Verificar contexto de empresa
        if (session.companyId == null) {
            call.respond(HttpStatusCode.BadRequest, "User has no company")
            return@post
        }
        
        val companyId = session.companyId // Ya sabemos que no es null por la validación anterior
        
        // 3. Recibir y validar datos
        val createRequest = try {
            val request = call.receive<CreateDocumentRequest>()
            request
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, "Invalid request format: ${e.message}")
            return@post
        }
        
        // 4. Usar service para crear documento (con lógica de negocio)
        try {
            val document = createRequest.toDomain(companyId, session.userId)
            val documentDTO = documentService.createDocument(document)
            call.respond(HttpStatusCode.Created, documentDTO)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Validation error")
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "Error creating document: ${e.message}")
        }
    }
    
    put("/{documentId}") {
        // 1. Validar sesión y autorización
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.Update.key
            )
        )
        
        // 2. Extraer y validar parámetros
        val documentIdString = call.parameters["documentId"]
        if (documentIdString == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing document ID")
            return@put
        }
        
        val documentId = try {
            UUID.fromString(documentIdString)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, "Invalid UUID format for document ID")
            return@put
        }
        
        // 3. Verificar contexto de empresa
        if (session.companyId == null) {
            call.respond(HttpStatusCode.BadRequest, "User has no company")
            return@put
        }
        
        val companyId = session.companyId // Ya sabemos que no es null por la validación anterior
        val userId = session.userId // Extraemos también el userId de la sesión

        // 4. Recibir y validar datos de actualización
        val updateRequest = try {
            val requestBody = call.receiveText()
            // Intentar deserializar manualmente
            Json.decodeFromString<UpdateDocumentRequest>(requestBody)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, "Invalid request format: ${e.message}")
            return@put
        }
        
        // 5. Usar service para actualizar documento (con lógica de negocio)
        try {
            // Primero obtenemos el documento existente
            val existingDocumentDTO = documentService.getDocumentById(documentId, companyId)
            if (existingDocumentDTO == null) {
                call.respond(HttpStatusCode.NotFound, "Document not found")
                return@put
            }
            
            // Convertimos el DTO de vuelta a Domain para aplicar cambios
            val existingDocument = existingDocumentDTO.toDomain()
            val updatedDocument = existingDocument.applyUpdate(updateRequest)
            
            val documentDTO = documentService.updateDocument(documentId, updatedDocument, companyId, userId)
            if (documentDTO != null) {
                call.respond(HttpStatusCode.OK, documentDTO)
            } else {
                call.respond(HttpStatusCode.NotFound, "Document not found")
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Validation error")
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "Error updating document: ${e.message}")
        }
    }
    
    delete("/{documentId}") {
        // 1. Validar sesión y autorización
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.Delete.key
            )
        )
        
        // 2. Extraer y validar parámetros
        val documentIdString = call.parameters["documentId"]
        if (documentIdString == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing document ID")
            return@delete
        }
        
        val documentId = try {
            UUID.fromString(documentIdString)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, "Invalid UUID format for document ID")
            return@delete
        }
        
        // 3. Verificar contexto de empresa
        if (session.companyId == null) {
            call.respond(HttpStatusCode.BadRequest, "User has no company")
            return@delete
        }
        
        val companyId = session.companyId // Ya sabemos que no es null por la validación anterior
        
        // 4. Usar service para eliminar documento (con lógica de negocio)
        try {
            val deleted = documentService.deleteDocument(documentId, companyId)
            if (deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Document not found")
            }
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Cannot delete document")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error deleting document: ${e.message}")
        }
    }
    
    // Endpoint para cambiar estado de documento
    patch("/{documentId}/status") {
        // 1. Validar sesión y autorización
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.Update.key
            )
        )
        
        // 2. Extraer y validar parámetros
        val documentIdString = call.parameters["documentId"]
        if (documentIdString == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing document ID")
            return@patch
        }
        
        val documentId = try {
            UUID.fromString(documentIdString)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, "Invalid document ID format")
            return@patch
        }
        
        // 3. Recibir request
        val statusRequest = try {
            call.receive<ChangeStatusRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format")
            return@patch
        }
        
        // 4. Usar service para cambiar estado
        try {
            // Validar que session tenga companyId (usuarios normales necesitan estar asociados a una compañía)
            val companyId = session.companyId
            val userId = session.userId // Extraemos también el userId de la sesión
            if (companyId == null) {
                call.respond(HttpStatusCode.BadRequest, "User must be associated with a company")
                return@patch
            }
            
            // Obtener documento existente
            val existingDocumentDTO = documentService.getDocumentById(documentId, companyId)
            if (existingDocumentDTO == null) {
                call.respond(HttpStatusCode.NotFound, "Document not found")
                return@patch
            }
            
            // Crear request de actualización solo para el estado
            val updateRequest = UpdateDocumentRequest(
                type = null,
                documentNumber = null,
                issueDate = null,
                dueDate = null,
                status = statusRequest.newStatus,
                currency = null,
                netAmount = null,
                taxAmount = null,
                totalAmount = null,
                fileUrl = null
            )
            
            // Aplicar cambios
            val existingDocument = existingDocumentDTO.toDomain()
            val updatedDocument = existingDocument.applyUpdate(updateRequest)
            
            val documentDTO = documentService.updateDocument(documentId, updatedDocument, companyId, userId)
            if (documentDTO != null) {
                call.respond(HttpStatusCode.OK, documentDTO)
            } else {
                call.respond(HttpStatusCode.NotFound, "Document not found")
            }
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Validation error")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error updating document status: ${e.message}")
        }
    }
    
    patch("/{documentId}/deactivate") {
        try {
            // 1. Validar sesión y autorización
            val session = authWrapper.authorizeOrThrow(
                call,
                requiredAnyPermissions = setOf(
                    DocumentManagementDefinition.Documents.Disable.key
                )
            )
            
            // 2. Verificar contexto de empresa
            val companyId = session.companyId
            if (companyId == null) {
                call.respond(HttpStatusCode.BadRequest, "User must be associated with a company")
                return@patch
            }
            
            // 3. Obtener ID del documento
            val documentIdParam = call.parameters["documentId"]
            if (documentIdParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Document ID is required")
                return@patch
            }
            
            val documentId = try {
                UUID.fromString(documentIdParam)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid document ID format")
                return@patch
            }
            
            // 4. Recibir request de desactivación
            val deactivateRequest = call.receive<DeactivateDocumentRequest>()
            
            // 5. Validar confirmación
            if (!deactivateRequest.confirm) {
                call.respond(HttpStatusCode.BadRequest, "Confirmation is required to deactivate document")
                return@patch
            }
            
            // 6. Desactivar documento
            val deactivatedDocument = documentService.deactivateDocument(
                documentId = documentId,
                companyId = companyId,
                userId = session.userId,
                reason = deactivateRequest.reason
            )
            
            if (deactivatedDocument != null) {
                val response = DeactivateDocumentResponse(
                    document = deactivatedDocument,
                    message = "Documento desactivado exitosamente",
                    deactivatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                )
                call.respond(HttpStatusCode.OK, response)
            } else {
                call.respond(HttpStatusCode.NotFound, "Document not found")
            }
            
        } catch (e: IllegalStateException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Cannot deactivate document")
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Validation error")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error deactivating document: ${e.message}")
        }
    }
}

@Serializable
data class ChangeStatusRequest(
    val newStatus: String
)
