package dev.byjtech.erp.modules.document_management.features.documents

import dev.byjtech.erp.document_management.dto.DocumentDTO

data class DocumentsFeatureState(
    val isLoading: Boolean = false,
    val documents: List<DocumentDTO> = emptyList(),
    val selectedDocument: DocumentDTO? = null,
    val error: String? = null,
    val message: String? = null
)
