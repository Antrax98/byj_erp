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
import dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus
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
    
    //se obtienen todos los documentos de la empresa
    get("/all") {
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.View.key
            )
        )
        
        //se valida que el usuario tenga una empresa asociada
        if (session.companyId == null) {
            call.respond(HttpStatusCode.BadRequest, "User has no company")
            return@get
        }
        
        val companyId = session.companyId
        
        try {
            val documentsDTO = documentService.getAllDocumentsByCompany(companyId)
            call.respond(HttpStatusCode.OK, documentsDTO)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "Error retrieving documents: ${e.message}")
        }
    }
    
    //se realiza búsqueda de documentos con filtros
    post("/search") {
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.View.key
            )
        )
        
        //se verifica contexto de empresa
        if (session.companyId == null) {
            call.respond(HttpStatusCode.BadRequest, "User has no company")
            return@post
        }
        
        //se reciben parámetros de búsqueda
        val searchRequest = try {
            call.receive<DocumentSearchRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid search request format")
            return@post
        }
        
        //se realiza búsqueda
        try {
            val searchResponse = documentService.searchDocuments(session.companyId, searchRequest)
            call.respond(HttpStatusCode.OK, searchResponse)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error performing search: ${e.message}")
        }
    }
    
    //se obtiene un documento específico por ID
    get("/{documentId}") {
        //se valida sesión y autorización
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.View.key
            )
        )
        
        //se extrae y valida parámetros
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
        
        //se verifica contexto de empresa
        if (session.companyId == null) {
            call.respond(HttpStatusCode.BadRequest, "User has no company")
            return@get
        }
        
        val companyId = session.companyId
        
        //se obtiene documento mediante el servicio
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
    
    //se crea un nuevo documento
    post {
        //se valida sesión y autorización
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.Create.key
            )
        )
        
        //se verifica contexto de empresa
        if (session.companyId == null) {
            call.respond(HttpStatusCode.BadRequest, "User has no company")
            return@post
        }
        
        val companyId = session.companyId
        
        //se recibe y valida datos del nuevo documento
        val createRequest = try {
            val request = call.receive<CreateDocumentRequest>()
            request
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, "Invalid request format: ${e.message}")
            return@post
        }
        
        //se crea documento mediante el servicio
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
    
    //se actualiza un documento existente
    put("/{documentId}") {
        //se valida sesión y autorización
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.Update.key
            )
        )
        
        //se extrae y valida parámetros
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
    
    //se elimina definitivamente un documento
    delete("/{documentId}") {
        //se valida sesión y autorización
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.Delete.key
            )
        )
        
        //se extrae y valida parámetros
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
        
        //se verifica contexto de empresa
        if (session.companyId == null) {
            call.respond(HttpStatusCode.BadRequest, "User has no company")
            return@delete
        }
        
        val companyId = session.companyId
        
        //se elimina documento mediante el servicio
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
    
    //se cambia el estado de un documento
    patch("/{documentId}/status") {
        //se valida sesión y autorización
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.Update.key
            )
        )
        
        //se extrae y valida parámetros
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
        
        //se recibe el request del cambio de estado
        val statusRequest = try {
            call.receive<ChangeStatusRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format")
            return@patch
        }
        
        //se cambia estado mediante el servicio
        try {
            //se valida que la sesión tenga empresa asociada
            val companyId = session.companyId
            val userId = session.userId
            if (companyId == null) {
                call.respond(HttpStatusCode.BadRequest, "User must be associated with a company")
                return@patch
            }
            
            //se valida que el nuevo estado es válido
            val newStatus = try {
                DocumentStatus.valueOf(statusRequest.newStatus.uppercase())
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid status: ${statusRequest.newStatus}")
                return@patch
            }
            
            //se actualiza el estado mediante el servicio específico
            val documentDTO = documentService.updateDocumentStatus(documentId, newStatus, companyId, userId)
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
    
    //se desactiva un documento (soft delete)
    patch("/{documentId}/deactivate") {
        try {
            //se valida sesión y autorización
            val session = authWrapper.authorizeOrThrow(
                call,
                requiredAnyPermissions = setOf(
                    DocumentManagementDefinition.Documents.Disable.key
                )
            )
            
            //se verifica contexto de empresa
            val companyId = session.companyId
            if (companyId == null) {
                call.respond(HttpStatusCode.BadRequest, "User must be associated with a company")
                return@patch
            }
            
            //se obtiene ID del documento
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
            // Log the full exception for debugging
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "Error deactivating document: ${e.message}")
        }
    }
}

@Serializable
data class ChangeStatusRequest(
    val newStatus: String
)
