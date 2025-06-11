package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolesMain

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import kotlinx.coroutines.flow.StateFlow

class RolesMainComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>
): RolesMainComponent, ComponentContext by componentContext {

}