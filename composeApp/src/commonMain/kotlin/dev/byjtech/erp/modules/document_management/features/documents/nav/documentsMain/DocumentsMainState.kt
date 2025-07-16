package dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain

import dev.byjtech.erp.document_management.dto.DocumentDTO

data class DocumentsMainState(
    val isLoading: Boolean = false,
    val documents: List<DocumentDTO> = emptyList(),
    val error: String? = null
)
