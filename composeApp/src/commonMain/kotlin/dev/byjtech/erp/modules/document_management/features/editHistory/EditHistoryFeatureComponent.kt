package dev.byjtech.erp.modules.document_management.features.editHistory

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.common.session.SessionManager
import dev.byjtech.erp.document_management.dto.DocumentEditHistoryDTO
import kotlinx.coroutines.flow.StateFlow

interface EditHistoryFeatureComponent : FeatureComponent {
    val state: StateFlow<EditHistoryFeatureState>
    val childStack: Value<ChildStack<*, Child>>
    
    fun loadEditHistory()
    
    sealed class Child {
        class Main(val component: EditHistoryMainComponent) : Child()
    }
}

data class EditHistoryFeatureState(
    val isLoading: Boolean = false,
    val editHistory: List<DocumentEditHistoryDTO> = emptyList(),
    val error: String? = null
)

interface EditHistoryMainComponent {
    val state: StateFlow<EditHistoryFeatureState>
    fun refresh()
}
