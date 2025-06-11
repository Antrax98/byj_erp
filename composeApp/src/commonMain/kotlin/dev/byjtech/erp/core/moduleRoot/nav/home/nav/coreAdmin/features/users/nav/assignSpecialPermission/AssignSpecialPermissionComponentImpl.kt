package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.assignSpecialPermission

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import kotlinx.coroutines.flow.StateFlow

class AssignSpecialPermissionComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val userIdToAssign: Int,
    override val assignablePermissions: Set<PermissionKey>,
    override val onFinished: (assigned: Boolean) -> Unit
): AssignSpecialPermissionComponent, ComponentContext by componentContext {

}