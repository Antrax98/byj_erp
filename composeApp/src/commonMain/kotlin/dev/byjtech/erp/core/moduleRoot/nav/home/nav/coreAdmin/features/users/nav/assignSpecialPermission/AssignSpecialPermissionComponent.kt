package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.assignSpecialPermission

import dev.byjtech.erp.common.PermissionAwareComponent
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.ModuleDTO
import kotlinx.coroutines.flow.StateFlow

interface AssignSpecialPermissionComponent: PermissionAwareComponent {
    val apiClient: ApiClient
    val userIdToAssign: String
    val actPermissions : Set<PermissionKey>
    val actModules : Set<ModuleDTO>
    val possiblePermissions : StateFlow<Map<String, Set<PermissionWithKey>>>
    val onFinished: (assigned: Boolean) -> Unit
    fun addPermission(permission: PermissionKey)
    fun loadPermissions()
}