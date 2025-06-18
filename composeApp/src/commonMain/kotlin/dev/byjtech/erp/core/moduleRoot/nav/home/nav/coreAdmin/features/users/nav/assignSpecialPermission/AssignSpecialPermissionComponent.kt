package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.assignSpecialPermission

import dev.byjtech.erp.common.PermissionAwareComponent
import dev.byjtech.erp.common.PermissionKey

interface AssignSpecialPermissionComponent: PermissionAwareComponent {
    val userIdToAssign: String
    val assignablePermissions: Set<PermissionKey>
    val onFinished: (assigned: Boolean) -> Unit
}