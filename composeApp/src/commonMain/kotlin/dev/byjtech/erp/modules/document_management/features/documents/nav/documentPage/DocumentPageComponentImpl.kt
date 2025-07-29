package dev.byjtech.erp.modules.document_management.features.documents.nav.documentPage

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.modules.document_management.features.documents.DocumentsFeatureComponentImpl
import kotlinx.coroutines.flow.StateFlow

class DocumentPageComponentImpl(
    override val componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val navTo: (DocumentsFeatureComponentImpl.Config) -> Unit,
    override val documentId: String,
    private val onNavigateBackCallback: () -> Unit,
    private val onDocumentDeactivatedCallback: () -> Unit
) : DocumentPageComponent {

    override fun onEditDocument(documentId: String) {
        navTo(DocumentsFeatureComponentImpl.Config.EditDocument(documentId))
    }

    override fun onNavigateBack() {
        onNavigateBackCallback()
    }

    override fun onDocumentDeactivated() {
        onDocumentDeactivatedCallback()
    }
}
