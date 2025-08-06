package dev.byjtech.erp.modules.document_management.features.documents.nav.documentHistory

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.modules.document_management.api.DocumentManagementClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DocumentHistoryComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val documentId: String,
    private val onNavigateBack: () -> Unit
) : DocumentHistoryComponent, ComponentContext by componentContext {

    private val coroutineScope = componentContext.coroutineScope()
    
    private val documentManagementClient = DocumentManagementClient(apiClient.clientKtor)

    private val _state = MutableStateFlow(DocumentHistoryState())
    override val state: StateFlow<DocumentHistoryState> = _state

    override fun loadHistory() {
        coroutineScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            try {
                // Cargar historial y resumen en paralelo
                val historyEntries = documentManagementClient.getDocumentEditHistory(documentId)
                val summary = documentManagementClient.getDocumentEditHistorySummary(documentId)
                
                _state.value = _state.value.copy(
                    isLoading = false,
                    historyEntries = historyEntries,
                    summary = summary,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error al cargar el historial: ${e.message}"
                )
            }
        }
    }

    override fun refreshHistory() {
        loadHistory()
    }

    override fun onBack() {
        onNavigateBack()
    }

    init {
        loadHistory()
    }
}
