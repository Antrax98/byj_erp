package dev.byjtech.erp.document_management.features.documents.nav.documentsMain

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.document_management.features.documents.DocumentsFeatureComponentImpl
import kotlinx.coroutines.flow.StateFlow

interface DocumentsMainComponent {
    val componentContext: ComponentContext
    val userPermissions: StateFlow<Set<PermissionKey>>
    val apiClient: ApiClient
    val navTo: (DocumentsFeatureComponentImpl.Config) -> Unit
    val state: StateFlow<DocumentsMainState>
    
    suspend fun loadDocuments()
    fun onDocumentClick(documentId: String)
    fun onAddDocumentClick()
}
