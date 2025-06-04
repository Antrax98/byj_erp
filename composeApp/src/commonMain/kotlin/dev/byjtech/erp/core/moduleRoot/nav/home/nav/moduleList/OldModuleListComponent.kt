package dev.byjtech.erp.core.moduleRoot.nav.home.nav.moduleList

import dev.byjtech.erp.common.old.OldModuleMetadata
import dev.byjtech.erp.common.old.OldModuleRootComponent

interface OldModuleListComponent: OldModuleRootComponent {
    val navTo: (String) -> Unit
    val modulesMetadata: Map<String, OldModuleMetadata>
}