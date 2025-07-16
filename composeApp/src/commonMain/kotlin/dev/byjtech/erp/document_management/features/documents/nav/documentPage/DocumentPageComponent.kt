package dev.byjtech.erp.document_management.features.documents.nav.documentPage

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.document_management.features.documents.DocumentsFeatureComponentImpl
import kotlinx.coroutines.flow.StateFlow

interface DocumentPageComponent {
    val componentContext: ComponentContext
    val userPermissions: StateFlow<Set<PermissionKey>>
    val apiClient: ApiClient
    val navTo: (DocumentsFeatureComponentImpl.Config) -> Unit
    val documentId: String
}
