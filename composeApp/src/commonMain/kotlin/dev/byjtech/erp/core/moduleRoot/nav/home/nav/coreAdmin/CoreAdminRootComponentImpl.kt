package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.api.CoreClient
import kotlinx.coroutines.flow.*

class CoreAdminRootComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<List<PermissionKey>>, //los permisos actuales del usuario
    override val apiClient: ApiClient,
    override val toHome: () -> Unit,
    private val coreClient: CoreClient
): CoreAdminRootComponent, ComponentContext by componentContext {

    override val moduleName: String
        get() = "core"


    //state del componente, solo hacerlo de ser necesario (probablemente no)
    private val _state = MutableStateFlow(CoreAdminRootState())
    override val state: StateFlow<CoreAdminRootState> = _state.asStateFlow()

    override suspend fun changeLoading() {
        _state.update { it.copy(isLoading = !it.isLoading) }
    }

}