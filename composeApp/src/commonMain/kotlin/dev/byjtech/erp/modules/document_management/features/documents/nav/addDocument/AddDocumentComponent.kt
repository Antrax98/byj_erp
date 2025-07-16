package dev.byjtech.erp.modules.document_management.features.documents.nav.addDocument

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.modules.document_management.features.documents.DocumentsFeatureComponentImpl
import kotlinx.coroutines.flow.StateFlow

interface AddDocumentComponent {
    val componentContext: ComponentContext
    val userPermissions: StateFlow<Set<PermissionKey>>
    val apiClient: ApiClient
    val navTo: (DocumentsFeatureComponentImpl.Config) -> Unit
}
