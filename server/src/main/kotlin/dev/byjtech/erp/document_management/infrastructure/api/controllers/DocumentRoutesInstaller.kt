package dev.byjtech.erp.document_management.infrastructure.api.controllers

import dev.byjtech.erp.document_management.domain.repository.DocumentRepository
import dev.byjtech.erp.document_management.infrastructure.exposed.extensions.toDTO
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import java.util.UUID

class DocumentRoutesInstaller(
    private val documentRepo: DocumentRepository
) : RoutesInstaller {
    
    override fun Route.installRoutes() {
        route("/documents") {
            documentsRoutes(documentRepo)
        }
    }
}

fun Route.documentsRoutes(documentRepo: DocumentRepository) {
    
    get("/all") {
        val documents = documentRepo.findAll()
        val documentsDTO = documents.map { it.toDTO() }
        call.respond(HttpStatusCode.OK, documentsDTO)
    }
    
    get("/{documentId}") {
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
        
        val document = documentRepo.findById(documentId)
        if (document == null) {
            call.respond(HttpStatusCode.NotFound, "Document not found")
            return@get
        }
        
        call.respond(HttpStatusCode.OK, document.toDTO())
    }
}
