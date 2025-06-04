package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin

import dev.byjtech.erp.common.old.OldModuleRootComponent
import kotlinx.coroutines.flow.StateFlow

interface CoreAdminRootComponentOld: OldModuleRootComponent {
    val state: StateFlow<CoreAdminRootState>
    suspend fun changeLoading()
}