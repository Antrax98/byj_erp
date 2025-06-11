package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.assignRole

import dev.byjtech.erp.common.PermissionAwareComponent
import dev.byjtech.erp.core.dto.RoleDTO

interface AssignRoleComponent: PermissionAwareComponent {
    val userIdToAssign: Int
    val assignableRoles: Set<RoleDTO>
    val onFinished: (assigned: Boolean) -> Unit
}