package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager
import kotlinx.coroutines.flow.*

class CoreAdminRootComponentImpl(
    componentContext: ComponentContext,
    override val sessionManager: SessionManager,
    override val apiClient: ApiClient,
    override val toHome: () -> Unit
): CoreAdminRootComponent, ComponentContext by componentContext {

    override val moduleName: String
        get() = "core"


    private val _state = MutableStateFlow(CoreAdminRootState())
    override val state: StateFlow<CoreAdminRootState> = _state.asStateFlow()

    override suspend fun changeLoading() {
        _state.update { it.copy(isLoading = !it.isLoading) }
    }
}