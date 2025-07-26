package dev.byjtech.erp.modules.document_management.api

import de.jensklingenberg.ktorfit.http.*
import dev.byjtech.erp.modules.document_management.request.DocumentSearchRequest
import dev.byjtech.erp.modules.document_management.dto.DocumentSearchResponse

interface DocumentSearchApi {
    
    @POST("api/document_management/documents/search")
    suspend fun searchDocuments(@Body searchRequest: DocumentSearchRequest): DocumentSearchResponse
    
}
