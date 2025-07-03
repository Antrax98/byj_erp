package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.userPage

import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.common.PermissionAwareComponent
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.UsersFeatureComponentImpl
import kotlinx.coroutines.flow.StateFlow

interface UserPageComponent: PermissionAwareComponent {
    val userId: String
    val apiClient: ApiClient
    val userInfo: StateFlow<UserDTO?>
    val userSpecialPermissions: StateFlow<List<PermissionWithKey>?>
    val userRoles: StateFlow<List<RoleDTO>?>
    val navTo: (UsersFeatureComponentImpl.Config) -> Unit
    fun fetchUserSpecialPermissions()
    fun deleteSpecialPermission(permissionId: String)
    fun deleteRole(roleId: String)
    fun fetchUserRoles()
    fun fetchUser()
}