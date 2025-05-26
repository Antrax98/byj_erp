package dev.byjtech.erp.core.moduleRoot.nav.home

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.common.ModuleEntry
import dev.byjtech.erp.common.ModuleManager
import dev.byjtech.erp.common.ModuleRootComponent
import kotlinx.coroutines.flow.StateFlow

interface HomeComponent {
    val state: StateFlow<HomeState>
    val childStack: Value<ChildStack<*, ModuleRootComponent>>
    val moduleManager: ModuleManager
    val entriesByName: Map<String, ModuleEntry>
    suspend fun onLogout()
    suspend fun onTestClick()
}