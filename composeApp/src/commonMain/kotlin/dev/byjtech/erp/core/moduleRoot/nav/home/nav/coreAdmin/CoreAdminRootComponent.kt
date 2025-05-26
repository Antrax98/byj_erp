package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin

import dev.byjtech.erp.common.ModuleRootComponent
import kotlinx.coroutines.flow.StateFlow

interface CoreAdminRootComponent: ModuleRootComponent {
    val state: StateFlow<CoreAdminRootState>
    suspend fun changeLoading()
}