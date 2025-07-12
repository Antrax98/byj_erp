package dev.byjtech.erp.document_management.infrastructure.api.controllers

import dev.byjtech.erp.document_management.DocumentManagementDefinition
import dev.byjtech.erp.document_management.domain.repository.DocumentRepository
import dev.byjtech.erp.document_management.application.service.DocumentService
import dev.byjtech.erp.document_management.infrastructure.auth.DocumentManagementAuthWrapper
import dev.byjtech.erp.document_management.infrastructure.exposed.extensions.toDTO
import dev.byjtech.erp.document_management.infrastructure.exposed.extensions.toDomain
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.request.receive
import kotlinx.serialization.Serializable
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
        // 1. Primero verificar si es SuperAdmin (no necesita permisos específicos ni compañía)
        val isSuperAdmin = try {
            authWrapper.authorizeOrThrow(call, requiredSuperAdmin = true)
            true
        } catch (e: Exception) {
            false
        }
        
        if (isSuperAdmin) {
            // SuperAdmin: acceso completo a todos los documentos
            try {
                val documentsDTO = documentService.getAllDocuments()
                call.respond(HttpStatusCode.OK, documentsDTO)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error retrieving documents: ${e.message}")
            }
            return@get
        }
        
        // 2. Para usuarios normales: validar sesión y permisos específicos
        val session = authWrapper.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.View.key
            )
        )
        
        // 3. Verificar contexto de empresa para usuarios normales
        if (session.companyId == null) {
            call.respond(HttpStatusCode.BadRequest, "User has no company")
            return@get
        }
        
        // 4. Usar service para obtener documentos por compañía
        try {
            val documentsDTO = documentService.getAllDocumentsByCompany(session.companyId)
            call.respond(HttpStatusCode.OK, documentsDTO)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error retrieving documents: ${e.message}")
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
        
        // 4. Usar service para obtener documento (con lógica de negocio)
        try {
            val documentDTO = documentService.getDocumentById(documentId, session.companyId)
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
        
        // 3. Recibir y validar datos
        val createRequest = try {
            call.receive<CreateDocumentRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format")
            return@post
        }
        
        // 4. Usar service para crear documento (con lógica de negocio)
        try {
            val document = createRequest.toDomain(session.companyId, session.userId)
            val documentDTO = documentService.createDocument(document)
            call.respond(HttpStatusCode.Created, documentDTO)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Validation error")
        } catch (e: Exception) {
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
        
        // 4. Recibir y validar datos de actualización
        val updateRequest = try {
            call.receive<UpdateDocumentRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format")
            return@put
        }
        
        // 5. Usar service para actualizar documento (con lógica de negocio)
        try {
            // Primero obtenemos el documento existente
            val existingDocumentDTO = documentService.getDocumentById(documentId, session.companyId)
            if (existingDocumentDTO == null) {
                call.respond(HttpStatusCode.NotFound, "Document not found")
                return@put
            }
            
            // Convertimos el DTO de vuelta a Domain para aplicar cambios
            val existingDocument = existingDocumentDTO.toDomain()
            val updatedDocument = updateRequest.applyTo(existingDocument)
            
            val documentDTO = documentService.updateDocument(documentId, updatedDocument, session.companyId)
            if (documentDTO != null) {
                call.respond(HttpStatusCode.OK, documentDTO)
            } else {
                call.respond(HttpStatusCode.NotFound, "Document not found")
            }
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Validation error")
        } catch (e: Exception) {
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
        
        // 4. Usar service para eliminar documento (con lógica de negocio)
        try {
            val deleted = documentService.deleteDocument(documentId, session.companyId)
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
}

// DTOs para requests
@Serializable
data class CreateDocumentRequest(
    val documentType: String,
    val documentNumber: String,
    val issueDate: String, // formato YYYY-MM-DD
    val dueDate: String?, // formato YYYY-MM-DD opcional
    val status: String,
    val currency: String,
    val netAmount: Double,
    val taxAmount: Double,
    val totalAmount: Double,
    val fileUrl: String?
) {
    fun toDomain(companyId: UUID, userId: UUID): dev.byjtech.erp.document_management.domain.model.Document {
        return dev.byjtech.erp.document_management.domain.model.Document(
            id = UUID.randomUUID(),
            documentType = this.documentType,
            documentNumber = this.documentNumber,
            companyId = companyId,
            issueDate = kotlinx.datetime.LocalDate.parse(this.issueDate),
            dueDate = this.dueDate?.let { kotlinx.datetime.LocalDate.parse(it) },
            status = dev.byjtech.erp.document_management.domain.model.DocumentStatus.valueOf(this.status),
            currency = this.currency,
            netAmount = this.netAmount,
            taxAmount = this.taxAmount,
            totalAmount = this.totalAmount,
            fileUrl = this.fileUrl ?: "",
            createdBy = userId,
            createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
            updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
            active = true
        )
    }
}

@Serializable
data class UpdateDocumentRequest(
    val documentType: String?,
    val documentNumber: String?,
    val issueDate: String?, // formato YYYY-MM-DD
    val dueDate: String?, // formato YYYY-MM-DD opcional
    val status: String?,
    val currency: String?,
    val netAmount: Double?,
    val taxAmount: Double?,
    val totalAmount: Double?,
    val fileUrl: String?
) {
    fun applyTo(
        existingDocument: dev.byjtech.erp.document_management.domain.model.Document
    ): dev.byjtech.erp.document_management.domain.model.Document {
        return existingDocument.copy(
            documentType = this.documentType ?: existingDocument.documentType,
            documentNumber = this.documentNumber ?: existingDocument.documentNumber,
            issueDate = this.issueDate?.let { kotlinx.datetime.LocalDate.parse(it) } ?: existingDocument.issueDate,
            dueDate = if (this.dueDate != null) kotlinx.datetime.LocalDate.parse(this.dueDate) else existingDocument.dueDate,
            status = this.status?.let { dev.byjtech.erp.document_management.domain.model.DocumentStatus.valueOf(it) } ?: existingDocument.status,
            currency = this.currency ?: existingDocument.currency,
            netAmount = this.netAmount ?: existingDocument.netAmount,
            taxAmount = this.taxAmount ?: existingDocument.taxAmount,
            totalAmount = this.totalAmount ?: existingDocument.totalAmount,
            fileUrl = this.fileUrl ?: existingDocument.fileUrl,
            updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
        )
    }
}
