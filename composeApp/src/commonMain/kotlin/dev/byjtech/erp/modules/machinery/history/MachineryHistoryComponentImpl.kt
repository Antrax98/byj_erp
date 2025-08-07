package dev.byjtech.erp.modules.machinery.history

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.modules.machinery.api.MachineryApiImpl
import dev.byjtech.erp.modules.machinery.dto.MachineryHistoryDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MachineryHistoryComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val toHome: () -> Unit,
    override val updateTitle: (newTitle: String) -> Unit,
    private val machineryId: String,
    private val machineryName: String
) : MachineryHistoryComponent, FeatureComponent, ComponentContext by componentContext {

    private val machineryApi = MachineryApiImpl(apiClient)
    private val coroutineScope = componentContext.coroutineScope()

    private val _state = MutableStateFlow(
        MachineryHistoryState(
            machineryId = machineryId,
            machineryName = machineryName
        )
    )
    override val state: StateFlow<MachineryHistoryState> = _state.asStateFlow()

    private val _historyList = MutableStateFlow<List<MachineryHistoryDTO>>(emptyList())
    override val historyList: StateFlow<List<MachineryHistoryDTO>> = _historyList.asStateFlow()

    init {
        updateTitle("Historial de $machineryName")
        loadHistory(machineryId)
    }

    override fun onBack(): Boolean {
        return false
    }

    override fun loadHistory(machineryId: String) {
        coroutineScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            machineryApi.getMachineryHistory(machineryId).fold(
                onSuccess = { history ->
                    _historyList.value = history
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = null
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error desconocido al cargar el historial"
                    )
                }
            )
        }
    }

    override fun refresh() {
        _state.value.machineryId?.let { id ->
            loadHistory(id)
        }
    }

    override fun clearMessages() {
        _state.value = _state.value.copy(error = null)
    }
}
