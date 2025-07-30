package dev.byjtech.erp.modules.document_management.features.editHistory

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

class EditHistoryFeatureComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    private val sessionManagerRef: SessionManager?,
    override val toHome: () -> Unit,
    override val updateTitle: (String) -> Unit
) : EditHistoryFeatureComponent, ComponentContext by componentContext {

    private val coroutineScope = componentContext.coroutineScope()
    
    private val _state = MutableStateFlow(EditHistoryFeatureState())
    override val state: StateFlow<EditHistoryFeatureState> = _state.asStateFlow()

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

    override val childStack: Value<ChildStack<Config, EditHistoryFeatureComponent.Child>> = stack

    override fun onBack(): Boolean {
        return false // Simple implementation - go back to previous screen
    }

    private fun child(config: Config, componentContext: ComponentContext): EditHistoryFeatureComponent.Child {
        return when (config) {
            is Config.Main -> EditHistoryFeatureComponent.Child.Main(
                EditHistoryMainComponentImpl(componentContext, _state, ::loadEditHistory)
            )
        }
    }

    override fun loadEditHistory() {
        coroutineScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val editHistory = apiClient.documentManagement.getAllEditHistory()
                _state.value = _state.value.copy(
                    isLoading = false,
                    editHistory = editHistory
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error al cargar historial: ${e.message}"
                )
            }
        }
    }

    init {
        updateTitle("Historial de Ediciones")
        loadEditHistory()
    }
}

class EditHistoryMainComponentImpl(
    componentContext: ComponentContext,
    override val state: StateFlow<EditHistoryFeatureState>,
    private val onRefresh: () -> Unit
) : EditHistoryMainComponent, ComponentContext by componentContext {

    override fun refresh() {
        onRefresh()
    }
}
