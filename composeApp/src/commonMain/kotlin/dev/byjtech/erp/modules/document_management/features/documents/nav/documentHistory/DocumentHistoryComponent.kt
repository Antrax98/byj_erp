package dev.byjtech.erp.modules.document_management.features.documents.nav.documentHistory

import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.document_management.dto.DocumentEditHistoryDTO
import kotlinx.coroutines.flow.StateFlow

interface DocumentHistoryComponent {
    val state: StateFlow<DocumentHistoryState>
    val userPermissions: StateFlow<Set<PermissionKey>>
    val apiClient: ApiClient
    val documentId: String

    fun loadHistory()
    fun refreshHistory()
    fun onBack()
}

data class DocumentHistoryState(
    val isLoading: Boolean = false,
    val historyEntries: List<DocumentEditHistoryDTO> = emptyList(),
    val summary: Map<String, Any> = emptyMap(),
    val error: String? = null
)
