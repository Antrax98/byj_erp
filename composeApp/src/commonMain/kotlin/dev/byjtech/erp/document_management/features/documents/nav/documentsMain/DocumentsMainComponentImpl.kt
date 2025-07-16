package dev.byjtech.erp.document_management.features.documents.nav.documentsMain

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.document_management.features.documents.DocumentsFeatureComponentImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DocumentsMainComponentImpl(
    override val componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val navTo: (DocumentsFeatureComponentImpl.Config) -> Unit
) : DocumentsMainComponent {

    private val coroutineScope = componentContext.coroutineScope()

    private val _state = MutableStateFlow(DocumentsMainState())
    override val state: StateFlow<DocumentsMainState> = _state

    override suspend fun loadDocuments() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val documents = apiClient.documentManagement.getAllDocuments()
            _state.value = _state.value.copy(
                isLoading = false,
                documents = documents
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = e.message ?: "Error desconocido"
            )
        }
    }

    override fun onDocumentClick(documentId: String) {
        navTo(DocumentsFeatureComponentImpl.Config.DocumentPage(documentId))
    }

    override fun onAddDocumentClick() {
        navTo(DocumentsFeatureComponentImpl.Config.AddDocument)
    }

    init {
        coroutineScope.launch {
            loadDocuments()
        }
    }
}
