package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.addUser

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import kotlinx.coroutines.flow.StateFlow

class AddUserComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val onFinished: (added: Boolean) -> Unit
): AddUserComponent, ComponentContext by componentContext {
    override val requiredPermissions: Set<PermissionKey> = setOf()


}