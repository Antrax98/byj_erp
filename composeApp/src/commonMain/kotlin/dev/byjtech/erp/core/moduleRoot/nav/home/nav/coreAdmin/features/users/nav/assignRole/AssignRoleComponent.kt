package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.assignRole

import dev.byjtech.erp.common.PermissionAwareComponent
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.RoleDTO
import kotlinx.coroutines.flow.StateFlow

interface AssignRoleComponent: PermissionAwareComponent {
    val apiClient: ApiClient
    val userIdToAssign: String
    val actUserRoles: Set<RoleDTO>
    val assignableRoles: StateFlow<Set<RoleDTO>>
    val onFinished: (assigned: Boolean) -> Unit
    fun fetchAssignableRoles()
    fun assignRole(roleId: String)

}