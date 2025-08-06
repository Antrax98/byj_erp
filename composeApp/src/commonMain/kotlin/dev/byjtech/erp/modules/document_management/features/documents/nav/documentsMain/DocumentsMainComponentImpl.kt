package dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.modules.document_management.features.documents.DocumentsFeatureComponentImpl
import dev.byjtech.erp.modules.document_management.request.DocumentSearchRequest
import dev.byjtech.erp.modules.document_management.api.DocumentManagementClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DocumentsMainComponentImpl(
    override val componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val navTo: (DocumentsFeatureComponentImpl.Config) -> Unit,
    override val notificationCount: StateFlow<Int>
) : DocumentsMainComponent {

    private val coroutineScope = componentContext.coroutineScope()
    
    private val documentManagementClient = DocumentManagementClient(apiClient.clientKtor)

    private val _state = MutableStateFlow(DocumentsMainState())
    override val state: StateFlow<DocumentsMainState> = _state

    override suspend fun loadDocuments() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val documents = documentManagementClient.getAllDocuments()
            _state.value = _state.value.copy(
                isLoading = false,
                documents = documents,
                isSearchMode = false
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = e.message ?: "Error desconocido"
            )
        }
    }

    override suspend fun searchDocuments(searchRequest: DocumentSearchRequest) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val searchResponse = documentManagementClient.searchDocuments(searchRequest)
            if (searchResponse != null) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    searchResponse = searchResponse,
                    documents = searchResponse.documents,
                    currentSearchRequest = searchRequest,
                    isSearchMode = true
                )
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error al realizar la búsqueda"
                )
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = e.message ?: "Error desconocido en la búsqueda"
            )
        }
    }

    override fun updateSearchRequest(searchRequest: DocumentSearchRequest) {
        _state.value = _state.value.copy(currentSearchRequest = searchRequest)
    }

    override fun onPageChange(page: Int) {
        val currentState = _state.value
        if (currentState.isSearchMode) {
            val newSearchRequest = currentState.currentSearchRequest.copy(page = page)
            coroutineScope.launch {
                searchDocuments(newSearchRequest)
            }
        }
    }

    override fun toggleSearchMode() {
        val currentState = _state.value
        if (currentState.isSearchMode) {
            // Volver al modo normal
            coroutineScope.launch {
                loadDocuments()
            }
        } else {
            // Activar modo búsqueda
            _state.value = currentState.copy(isSearchMode = true)
        }
    }

    override fun onDocumentClick(documentId: String) {
        navTo(DocumentsFeatureComponentImpl.Config.DocumentPage(documentId))
    }

    override fun onAddDocumentClick() {
        navTo(DocumentsFeatureComponentImpl.Config.AddDocument)
    }

    override fun onEditDocumentClick(documentId: String) {
        navTo(DocumentsFeatureComponentImpl.Config.EditDocument(documentId))
    }

    override fun onViewHistoryClick(documentId: String) {
        navTo(DocumentsFeatureComponentImpl.Config.DocumentHistory(documentId))
    }

    override fun onDeleteDocumentClick(documentId: String) {
        // TODO: Implementar confirmación y eliminación
        println("Eliminar documento: $documentId")
    }

    override fun onNotificationsClick() {
        navTo(DocumentsFeatureComponentImpl.Config.Notifications)
    }

    override fun onNotificationSettingsClick() {
        navTo(DocumentsFeatureComponentImpl.Config.NotificationSettings)
    }

    init {
        coroutineScope.launch {
            loadDocuments()
        }
    }
}
