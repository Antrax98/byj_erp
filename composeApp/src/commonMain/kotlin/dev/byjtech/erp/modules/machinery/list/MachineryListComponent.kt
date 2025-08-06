package dev.byjtech.erp.modules.machinery.list

import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.modules.machinery.dto.MachineryDTO
import kotlinx.coroutines.flow.StateFlow

interface MachineryListComponent : FeatureComponent {
    val state: StateFlow<MachineryListState>
    val machineryList: StateFlow<List<MachineryDTO>>
    
    fun loadMachinery()
    fun loadInactiveMachinery()
    fun refresh()
    fun onMachineryClick(machinery: MachineryDTO)
    fun onEditMachinery(machinery: MachineryDTO)
    fun onDeactivateMachinery(machinery: MachineryDTO)
    fun onActivateMachinery(machinery: MachineryDTO)
    fun clearMessages()
}

data class MachineryListState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val showingInactive: Boolean = false,
    val successMessage: String? = null
)
