package dev.byjtech.erp.core.moduleRoot.nav.home

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.common.ComponentConfig
import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.common.ModuleManager
import dev.byjtech.erp.common.old.OldModuleEntry
import dev.byjtech.erp.common.old.OldModuleManager
import dev.byjtech.erp.common.old.OldModuleRootComponent
import dev.byjtech.erp.core.dto.UserDTO
import kotlinx.coroutines.flow.StateFlow

interface HomeComponent {
    val state: StateFlow<HomeState>
    val childStack: Value<ChildStack<ComponentConfig, FeatureComponent>>
    val screenMap: Map<String,Map<String, @Composable (FeatureComponent) -> Unit>>
    val moduleManager: ModuleManager
    val actualUser: StateFlow<UserDTO?>
    fun onBack()
    suspend fun onLogout()
    suspend fun onTestClick()
}