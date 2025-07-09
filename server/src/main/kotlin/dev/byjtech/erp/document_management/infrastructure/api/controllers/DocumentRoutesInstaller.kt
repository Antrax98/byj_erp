package dev.byjtech.erp.document_management.infrastructure.api.controllers

import dev.byjtech.erp.document_management.domain.repository.DocumentRepository
import dev.byjtech.erp.document_management.infrastructure.exposed.extensions.toDTO
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.document_management.DocumentManagementDefinition
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import java.util.UUID

class DocumentRoutesInstaller(
    private val documentRepo: DocumentRepository,
    private val authServ: CoreAuthWrapper
) : RoutesInstaller {
    
    override fun Route.installRoutes() {
        route("/documents") {
            route("/tenant") {
                tenantDocuments(authServ, documentRepo)
            }
            route("/super-admin") {
                // placeholder para rutas de super admin
            }
        }
    }
}

fun Route.tenantDocuments(authServ: CoreAuthWrapper, documentRepo: DocumentRepository) {
    
    get("/all") {
        authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.View.key
            )
        )
        val documents = documentRepo.findAll()
        val documentsDTO = documents.map { it.toDTO() }
        call.respond(HttpStatusCode.OK, documentsDTO)
    }
    
    get("/{documentId}") {
        authServ.authorizeOrThrow(
            call,
            requiredAnyPermissions = setOf(
                DocumentManagementDefinition.Documents.View.key
            )
        )
        val documentId = call.parameters["documentId"]
        if (documentId == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing document ID")
            return@get
        }
        
        val document = documentRepo.findById(UUID.fromString(documentId))
        if (document == null) {
            call.respond(HttpStatusCode.NotFound, "Document not found")
            return@get
        }
        
        call.respond(HttpStatusCode.OK, document.toDTO())
    }
}
