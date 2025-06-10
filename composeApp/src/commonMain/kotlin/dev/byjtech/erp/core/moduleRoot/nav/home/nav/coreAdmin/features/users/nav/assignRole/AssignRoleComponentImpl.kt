package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.assignRole

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.core.CoreDefinition
import kotlinx.coroutines.flow.StateFlow

class AssignRoleComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
): AssignRoleComponent, ComponentContext by componentContext {
    override val requiredPermissions: Set<PermissionKey> = setOf(
        CoreDefinition.Roles.Assign.key,
        CoreDefinition.Admin.All.key
    )
}