package dev.byjtech.erp.document_management.infrastructure.api.controllers

import dev.byjtech.erp.document_management.DocumentManagementDefinition
import dev.byjtech.erp.document_management.domain.repository.DocumentRepository
import dev.byjtech.erp.document_management.infrastructure.auth.DocumentManagementAuthWrapper
import dev.byjtech.erp.document_management.infrastructure.exposed.extensions.toDTO
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import java.util.UUID

class DocumentRoutesInstaller(
    private val documentRepo: DocumentRepository,
    private val authWrapper: DocumentManagementAuthWrapper
) : RoutesInstaller {
    
    override fun Route.installRoutes() {
        route("/documents") {
            documentsRoutes(documentRepo, authWrapper)
        }
    }
}

fun Route.documentsRoutes(
    documentRepo: DocumentRepository, 
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
        
        // 3. Obtener documentos filtrados por companyId
        val documents = documentRepo.findByCompanyId(session.companyId)
        val documentsDTO = documents.map { it.toDTO() }
        
        // 4. Responder con éxito
        call.respond(HttpStatusCode.OK, documentsDTO)
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
        
        // 3. Obtener documento y verificar que pertenece a la empresa
        val document = documentRepo.findById(documentId)
        if (document == null || document.companyId != session.companyId) {
            call.respond(HttpStatusCode.NotFound, "Document not found")
            return@get
        }
        
        // 4. Responder con éxito
        call.respond(HttpStatusCode.OK, document.toDTO())
    }
}
