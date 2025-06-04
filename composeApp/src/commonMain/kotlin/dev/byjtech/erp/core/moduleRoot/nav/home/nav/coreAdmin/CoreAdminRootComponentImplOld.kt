package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.api.CoreClient
import kotlinx.coroutines.flow.*

class CoreAdminRootComponentImplOld(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>, //los permisos actuales del usuario
    override val apiClient: ApiClient,
    override val toHome: () -> Unit,
    private val coreClient: CoreClient
): CoreAdminRootComponentOld, ComponentContext by componentContext {

    override val moduleName: String
        get() = "core"


    //state del componente, solo hacerlo de ser necesario (probablemente no)
    private val _state = MutableStateFlow(CoreAdminRootState())
    override val state: StateFlow<CoreAdminRootState> = _state.asStateFlow()

    override suspend fun changeLoading() {
        _state.update { it.copy(isLoading = !it.isLoading) }
    }

    //NAVEGACION
    private val navigation = StackNavigation<String>()

//    private fun childFactory(config: String, componentContext: ComponentContext): ModuleRootComponent {
//        return when (config) {
//            "home" -> ModuleListComponentImpl(
//                componentContext.childContext("home_moduleList"),
//                sessionManager.userPermissions,
//                api,
//                toHome = ::toHome,
//                navTo = ::navigateTo,
//                modulesMetadata = moduleManager.metadataMap()
//            )
//            else -> {
//                val factory = entriesByName[config]?.factory
//                    ?: throw IllegalArgumentException("Invalid config: $config")
//
//                factory.create(componentContext.childContext(config), sessionManager.userPermissions, api, ::toHome)
//            }
//        }
//    }

}