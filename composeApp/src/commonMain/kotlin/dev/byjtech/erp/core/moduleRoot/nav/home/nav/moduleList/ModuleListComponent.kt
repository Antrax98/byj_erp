package dev.byjtech.erp.core.moduleRoot.nav.home.nav.moduleList

import dev.byjtech.erp.common.ModuleMetadata
import dev.byjtech.erp.common.ModuleRootComponent
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager

interface ModuleListComponent: ModuleRootComponent {
    val navTo: (String) -> Unit
    val modulesMetadata: Map<String, ModuleMetadata>
}