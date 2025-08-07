package dev.byjtech.erp.modules.machinery.history

import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.modules.machinery.dto.MachineryHistoryDTO
import kotlinx.coroutines.flow.StateFlow

interface MachineryHistoryComponent : FeatureComponent {
    val state: StateFlow<MachineryHistoryState>
    val historyList: StateFlow<List<MachineryHistoryDTO>>
    
    fun loadHistory(machineryId: String)
    fun refresh()
    fun clearMessages()
}

data class MachineryHistoryState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val machineryId: String? = null,
    val machineryName: String? = null
)
