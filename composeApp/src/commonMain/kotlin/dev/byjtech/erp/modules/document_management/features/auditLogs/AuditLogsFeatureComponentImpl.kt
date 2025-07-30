package dev.byjtech.erp.modules.document_management.features.auditLogs

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class AuditLogsFeatureComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    private val sessionManagerRef: SessionManager?,
    override val toHome: () -> Unit,
    override val updateTitle: (String) -> Unit
) : AuditLogsFeatureComponent, ComponentContext by componentContext {

    private val coroutineScope = componentContext.coroutineScope()
    
    private val _state = MutableStateFlow(AuditLogsFeatureState())
    override val state: StateFlow<AuditLogsFeatureState> = _state.asStateFlow()

    @Serializable
    sealed class Config {
        @Serializable
        data object Main : Config()
    }

    private val navigation = StackNavigation<Config>()

    private val stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Main) },
        handleBackButton = true,
        childFactory = ::child
    )

    override val childStack: Value<ChildStack<Config, AuditLogsFeatureComponent.Child>> = stack

    override fun onBack(): Boolean {
        return false // Simple implementation - go back to previous screen
    }

    private fun child(config: Config, componentContext: ComponentContext): AuditLogsFeatureComponent.Child {
        return when (config) {
            is Config.Main -> AuditLogsFeatureComponent.Child.Main(
                AuditLogsMainComponentImpl(componentContext, _state, ::loadAuditLogs)
            )
        }
    }

    override fun loadAuditLogs() {
        coroutineScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val auditLogs = apiClient.documentManagement.getAllAuditLogs()
                _state.value = _state.value.copy(
                    isLoading = false,
                    auditLogs = auditLogs
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error al cargar logs de auditoría: ${e.message}"
                )
            }
        }
    }

    init {
        updateTitle("Logs de Auditoría")
        loadAuditLogs()
    }
}

class AuditLogsMainComponentImpl(
    componentContext: ComponentContext,
    override val state: StateFlow<AuditLogsFeatureState>,
    private val onRefresh: () -> Unit
) : AuditLogsMainComponent, ComponentContext by componentContext {

    override fun refresh() {
        onRefresh()
    }
}
