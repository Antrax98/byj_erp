package dev.byjtech.erp.core.moduleRoot.nav.home

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager
import kotlinx.coroutines.flow.*

class HomeComponentImpl(
    componentContext: ComponentContext,
    private val sessionManager: SessionManager,
    private val api: ApiClient
): HomeComponent, ComponentContext by componentContext {

    override val moduleName = "core"

    private val _state = MutableStateFlow(HomeState())
    override val state: StateFlow<HomeState> = _state.asStateFlow()

    override suspend fun onLogout() {
        _state.update { it.copy(isLoading = true) }
        try {
            sessionManager.logout()
            _state.update { it.copy(isLoading = false, message = "Sesión cerrada") }
        } catch (e: Exception) {
            _state.update { it.copy(isLoading = false, error = e.message) }
        }
    }

    override suspend fun onTestClick() {
        _state.update { it.copy(isLoading = true) }
        try {
            api.coreAuth.test()
            _state.update { it.copy(isLoading = false, message = "Petición exitosa") }
        } catch (e: Exception) {
            _state.update { it.copy(isLoading = false, error = e.message) }
        }
    }
}