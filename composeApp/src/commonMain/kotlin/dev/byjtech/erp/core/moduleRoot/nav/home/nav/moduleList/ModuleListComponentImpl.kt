package dev.byjtech.erp.core.moduleRoot.nav.home.nav.moduleList

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.ModuleMetadata
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager
import kotlinx.coroutines.flow.StateFlow

//hacer que este componente no herede de ModuleRootCOmponent de ser necesario
class ModuleListComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val toHome: () -> Unit,
    override val navTo: (String) -> Unit,
    override val modulesMetadata: Map<String, ModuleMetadata>
): ModuleListComponent, ComponentContext by componentContext {


    override val moduleName: String
        get() = "core"



}