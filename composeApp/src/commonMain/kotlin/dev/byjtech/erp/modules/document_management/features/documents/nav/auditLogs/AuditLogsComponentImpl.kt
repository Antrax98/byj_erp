package dev.byjtech.erp.modules.document_management.features.documents.nav.auditLogs

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.modules.document_management.api.DocumentManagementClient

class AuditLogsComponentImpl(
    componentContext: ComponentContext,
    private val userPermissions: StateFlow<Set<PermissionKey>>,
    private val apiClient: DocumentManagementClient,
    private val onNavigateBack: () -> Unit
) : AuditLogsComponent, ComponentContext by componentContext {
    
    fun onBackPressed() {
        onNavigateBack()
    }
}
