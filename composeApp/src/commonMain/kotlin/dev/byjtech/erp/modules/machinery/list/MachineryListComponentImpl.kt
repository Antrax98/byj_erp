package dev.byjtech.erp.modules.machinery.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.modules.machinery.MachineryDefinition
import dev.byjtech.erp.modules.machinery.api.MachineryApiImpl
import dev.byjtech.erp.modules.machinery.dto.MachineryDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MachineryListComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val toHome: () -> Unit,
    override val updateTitle: (newTitle: String) -> Unit
) : MachineryListComponent, FeatureComponent, ComponentContext by componentContext {

    // Crear la API directamente usando el ApiClient
    private val machineryApi = MachineryApiImpl(apiClient)
    private val coroutineScope = componentContext.coroutineScope()

    private val _state = MutableStateFlow(MachineryListState())
    override val state: StateFlow<MachineryListState> = _state.asStateFlow()

    private val _machineryList = MutableStateFlow<List<MachineryDTO>>(emptyList())
    override val machineryList: StateFlow<List<MachineryDTO>> = _machineryList.asStateFlow()

    override fun onBack(): Boolean {
        // Since this is a simple list without nested navigation, return false to let parent handle
        return false
    }

    override fun loadMachinery() {
        coroutineScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            machineryApi.getAllMachinery().fold(
                onSuccess = { machineries ->
                    _machineryList.value = machineries
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = null
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error desconocido"
                    )
                }
            )
        }
    }

    override fun refresh() {
        loadMachinery()
    }

    override fun onMachineryClick(machinery: MachineryDTO) {
        // TODO: Implement machinery detail navigation
        println("Clicked on machinery: ${machinery.name}")
    }

    init {
        updateTitle("Maquinaria")
        loadMachinery()
    }
}
