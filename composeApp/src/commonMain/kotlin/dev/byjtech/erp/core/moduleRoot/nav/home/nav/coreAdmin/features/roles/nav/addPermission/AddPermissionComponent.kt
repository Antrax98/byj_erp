package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addPermission

import dev.byjtech.erp.common.PermissionAwareComponent
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.ModuleDTO
import kotlinx.coroutines.flow.StateFlow

interface AddPermissionComponent: PermissionAwareComponent {
    val apiClient : ApiClient
    val actPermissions : Set<PermissionKey>
    val actModules : Set<ModuleDTO>
    val possiblePermissions : StateFlow<Map<String,Set<PermissionWithKey>>>
    val isLoading : StateFlow<Boolean>
    val roleId : String
    val onFinished: (added: Boolean) -> Unit
    fun addPermission(permission: PermissionKey)
    suspend fun loadPermissions()
}