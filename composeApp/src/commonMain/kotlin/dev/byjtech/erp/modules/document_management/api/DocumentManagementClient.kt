package dev.byjtech.erp.modules.document_management.api

import dev.byjtech.erp.document_management.dto.DocumentDTO
import dev.byjtech.erp.document_management.dto.DocumentAuditLogDTO
import dev.byjtech.erp.document_management.dto.DocumentEditHistoryDTO
import dev.byjtech.erp.document_management.request.CreateDocumentRequest
import dev.byjtech.erp.document_management.request.UpdateDocumentRequest
import dev.byjtech.erp.modules.document_management.request.DocumentSearchRequest
import dev.byjtech.erp.modules.document_management.dto.DocumentSearchResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class DocumentManagementClient(private val client: HttpClient) {
    
    // Documents endpoints
    suspend fun getAllDocuments(): List<DocumentDTO> {
        return try {
            client.get(urlString = "api/document_management/documents/all").body()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun searchDocuments(searchRequest: DocumentSearchRequest): DocumentSearchResponse? {
        return try {
            client.post(urlString = "api/document_management/documents/search") {
                contentType(ContentType.Application.Json)
                setBody(searchRequest)
            }.body()
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun getDocumentById(documentId: String): DocumentDTO? {
        return try {
            client.get(urlString = "api/document_management/documents/$documentId").body()
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun createDocument(request: CreateDocumentRequest): DocumentDTO? {
        return try {
            println("🔍 Enviando request: $request")
            val response = client.post(urlString = "api/document_management/documents") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            println("🔍 Response status: ${response.status}")
            response.body()
        } catch (e: Exception) {
            println("❌ Error en createDocument: ${e.message}")
            e.printStackTrace()
            null
        }
    }
    
    suspend fun updateDocument(
        documentId: String,
        request: UpdateDocumentRequest
    ): DocumentDTO? {
        return try {
            client.put(urlString = "api/document_management/documents/$documentId") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun deleteDocument(documentId: String): Boolean {
        return try {
            client.delete(urlString = "api/document_management/documents/$documentId")
            true
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun changeDocumentStatus(
        documentId: String,
        newStatus: String
    ): DocumentDTO? {
        return try {
            client.patch(urlString = "api/document_management/documents/$documentId/status") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("newStatus" to newStatus))
            }.body()
        } catch (e: Exception) {
            null
        }
    }
    
    // Audit logs endpoints
    suspend fun getDocumentAuditLogs(documentId: String): List<DocumentAuditLogDTO> {
        return try {
            client.get(urlString = "api/document_management/documents/$documentId/audit-logs").body()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Edit history endpoints
    suspend fun getDocumentEditHistory(documentId: String): List<DocumentEditHistoryDTO> {
        return try {
            client.get(urlString = "api/document_management/documents/$documentId/edit-history").body()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
