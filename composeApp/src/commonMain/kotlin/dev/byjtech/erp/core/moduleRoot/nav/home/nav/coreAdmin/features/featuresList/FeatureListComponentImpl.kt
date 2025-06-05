package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.featuresList

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.ButtonMetadata
import dev.byjtech.erp.common.ComponentConfig
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FeatureListComponentImpl(
    componentContext: ComponentContext,
    override val apiClient: ApiClient,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val buttonsMap: Map<String, Map<String, ButtonMetadata>>,
    override val toHome: () -> Unit,
    override val navTo: (ComponentConfig) -> Unit
): FeatureListComponent, ComponentContext by componentContext {

    private val _state = MutableStateFlow(FeatureListState())
    override val state: StateFlow<FeatureListState> = _state.asStateFlow()

    override fun onBack(): Boolean {
        return false
        //no hace nada por que este no deveria de hacerlo
        //toHome esta de bonito en este componente
    }
}