package dev.byjtech.erp.modules.document_management.features.auditLogs

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.common.session.SessionManager
import dev.byjtech.erp.document_management.dto.DocumentAuditLogDTO
import kotlinx.coroutines.flow.StateFlow

interface AuditLogsFeatureComponent : FeatureComponent {
    val state: StateFlow<AuditLogsFeatureState>
    val childStack: Value<ChildStack<*, Child>>
    
    fun loadAuditLogs()
    
    sealed class Child {
        class Main(val component: AuditLogsMainComponent) : Child()
    }
}

data class AuditLogsFeatureState(
    val isLoading: Boolean = false,
    val auditLogs: List<DocumentAuditLogDTO> = emptyList(),
    val error: String? = null
)

interface AuditLogsMainComponent {
    val state: StateFlow<AuditLogsFeatureState>
    fun refresh()
}
