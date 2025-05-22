package dev.byjtech.erp.core.moduleRoot.nav.home

import dev.byjtech.erp.common.ModuleRootComponent
import kotlinx.coroutines.flow.StateFlow

interface HomeComponent : ModuleRootComponent {
    val state: StateFlow<HomeState>
    suspend fun onLogout()
    suspend fun onTestClick()
}