package dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain

import dev.byjtech.erp.document_management.dto.DocumentDTO
import dev.byjtech.erp.modules.document_management.request.DocumentSearchRequest
import dev.byjtech.erp.modules.document_management.dto.DocumentSearchResponse

data class DocumentsMainState(
    val isLoading: Boolean = false,
    val documents: List<DocumentDTO> = emptyList(),
    val searchResponse: DocumentSearchResponse? = null,
    val currentSearchRequest: DocumentSearchRequest = DocumentSearchRequest(),
    val error: String? = null,
    val isSearchMode: Boolean = false
)
