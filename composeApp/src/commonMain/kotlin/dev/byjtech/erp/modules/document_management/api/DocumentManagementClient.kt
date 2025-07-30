package dev.byjtech.erp.modules.document_management.api

import dev.byjtech.erp.document_management.dto.DocumentDTO
import dev.byjtech.erp.document_management.dto.DocumentAuditLogDTO
import dev.byjtech.erp.document_management.dto.DocumentEditHistoryDTO
import dev.byjtech.erp.document_management.request.CreateDocumentRequest
import dev.byjtech.erp.document_management.request.UpdateDocumentRequest
import dev.byjtech.erp.document_management.request.DeactivateDocumentRequest
import dev.byjtech.erp.document_management.response.DeactivateDocumentResponse
import dev.byjtech.erp.modules.document_management.request.DocumentSearchRequest
import dev.byjtech.erp.modules.document_management.dto.DocumentSearchResponse
import dev.byjtech.erp.common.ApiResponse
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
            println("🚀 Enviando request de actualización: documentId=$documentId, request=$request")
            val response = client.put(urlString = "api/document_management/documents/$documentId") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            println("🔍 Response status: ${response.status}")
            
            when (response.status.value) {
                200 -> {
                    response.body<DocumentDTO>()
                }
                else -> {
                    val errorBody = try {
                        response.body<String>()
                    } catch (e: Exception) {
                        "Unable to read error response"
                    }
                    println("❌ Server error (${response.status}): $errorBody")
                    null
                }
            }
        } catch (e: Exception) {
            println("❌ Error en updateDocument: ${e.message}")
            e.printStackTrace()
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
    
    // Edit history endpoints
    suspend fun getDocumentEditHistory(documentId: String): List<DocumentEditHistoryDTO> {
        return try {
            client.get(urlString = "api/document_management/documents/$documentId/edit-history").body()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getDocumentEditHistorySummary(documentId: String): Map<String, Any> {
        return try {
            client.get(urlString = "api/document_management/documents/$documentId/edit-history/summary").body()
        } catch (e: Exception) {
            emptyMap()
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
    
    // Global endpoints for main page
    suspend fun getAllEditHistory(): List<DocumentEditHistoryDTO> {
        return try {
            client.get(urlString = "api/document_management/edit-history/all").body()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getAllAuditLogs(): List<DocumentAuditLogDTO> {
        return try {
            client.get(urlString = "api/document_management/audit-logs/all").body()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Deactivate document endpoint
    suspend fun deactivateDocument(documentId: String, request: DeactivateDocumentRequest): ApiResponse<DeactivateDocumentResponse, String> {
        return try {
            val response = client.patch(urlString = "api/document_management/documents/$documentId/deactivate") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            ApiResponse.Success(response.body())
        } catch (e: Exception) {
            ApiResponse.Error("Error al desactivar documento", e.message ?: "Error desconocido")
        }
    }
}
