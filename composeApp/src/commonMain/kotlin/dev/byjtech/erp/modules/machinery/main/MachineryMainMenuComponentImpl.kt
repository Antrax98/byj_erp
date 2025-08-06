package dev.byjtech.erp.modules.machinery.main

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.modules.machinery.MachineryDefinition
import kotlinx.coroutines.flow.StateFlow

class MachineryMainMenuComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    private val onNavigateToList: () -> Unit,
    private val onNavigateToInactiveList: () -> Unit,
    private val onNavigateToCreate: () -> Unit
) : MachineryMainMenuComponent, ComponentContext by componentContext {
    
    override val requiredPermissions: Set<PermissionKey> = emptySet()
    override val optionalPermissions: Set<PermissionKey> = setOf(
        MachineryDefinition.Machinery.View.key,
        MachineryDefinition.Machinery.Create.key
    )

    override fun onViewMachineriesClick() {
        onNavigateToList()
    }

    override fun onViewInactiveMachineriesClick() {
        onNavigateToInactiveList()
    }

    override fun onCreateMachineryClick() {
        onNavigateToCreate()
    }
}
