package dev.byjtech.erp.modules.document_management.features.documents.nav.editHistory

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.modules.document_management.api.DocumentManagementClient

class EditHistoryComponentImpl(
    componentContext: ComponentContext,
    private val userPermissions: StateFlow<Set<PermissionKey>>,
    private val apiClient: DocumentManagementClient,
    private val onNavigateBack: () -> Unit
) : EditHistoryComponent, ComponentContext by componentContext {
    
    fun onBackPressed() {
        onNavigateBack()
    }
}
