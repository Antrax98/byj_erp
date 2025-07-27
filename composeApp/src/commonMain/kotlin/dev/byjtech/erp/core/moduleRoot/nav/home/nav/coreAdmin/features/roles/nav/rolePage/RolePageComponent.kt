package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolePage

import dev.byjtech.erp.common.PermissionAwareComponent
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.RolesFeatureComponentImpl.Config
import kotlinx.coroutines.flow.StateFlow

interface RolePageComponent: PermissionAwareComponent {
    val roleId: String
    val apiClient: ApiClient
    val roleInfo: StateFlow<RoleDTO?>
    val rolePermissions: StateFlow<Set<PermissionWithKey>>
    val isLoading: StateFlow<Boolean>
    val isPermissionLoading: StateFlow<Boolean>
    val isBusy: StateFlow<Boolean>
    val navTo: (Config) -> Unit
    fun deletePermission(permissionId: String)
    fun fetchRole()
    fun fetchRolePermissions()
    fun updateRoleName(name: String)
}