package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.usersMain.nav.userPage

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.dto.UserDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPageComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val userId: Int,
    override val apiClient: ApiClient

): UserPageComponent, ComponentContext by componentContext {
    override val requiredPermissions: Set<PermissionKey> =
        setOf(
            CoreDefinition.Admin.All.key,
            CoreDefinition.Users.View.key
        )
    override val optionalPermissions: Set<PermissionKey> =
        setOf(
            CoreDefinition.Users.Update.key
        )

    private val _userInfo = MutableStateFlow<UserDTO?>(null)
    override val userInfo: StateFlow<UserDTO?> = _userInfo.asStateFlow()

    override suspend fun fetchUser() {
        val userResponse = apiClient.usersTenantApi.getUser(userId)
        if (userResponse != null) {
            _userInfo.value = userResponse
        }

    }

    private val _userSpecialPermissions = MutableStateFlow<List<PermissionKey>?>(null)
    override val userSpecialPermissions: StateFlow<List<PermissionKey>?> = _userSpecialPermissions.asStateFlow()

    override suspend fun fetchUserSpecialPermissions() {
        val userSpecialPermissionsResponse = apiClient.coreAuth.getSpecialPermissionsByUserId(userId)
        _userSpecialPermissions.value = userSpecialPermissionsResponse.permissionKeys

    }

    private val _userRoles = MutableStateFlow<List<RoleDTO>?>(null)
    override val userRoles: StateFlow<List<RoleDTO>?> = _userRoles.asStateFlow()

    override suspend fun fetchUserRoles() {
        val userRolesResponse = apiClient.coreAuth.userRoles(userId)
        _userRoles.value = userRolesResponse.roles
    }

}