package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.userPage

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.UsersFeatureComponentImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserPageComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val userId: String,
    override val apiClient: ApiClient,
    override val navTo: (UsersFeatureComponentImpl.Config) -> Unit
): UserPageComponent, ComponentContext by componentContext {
    override val requiredPermissions: Set<PermissionKey> =
        setOf(
            CoreDefinition.Admin.All.key,
            CoreDefinition.Users.View.key
        )
    override val optionalPermissions: Set<PermissionKey> =
        setOf(
            CoreDefinition.Users.Update.key,
            CoreDefinition.Roles.Delete.key,
            CoreDefinition.Roles.Assign.key
        )

    private val coroutineScope = componentContext.coroutineScope()

    private val _userInfo = MutableStateFlow<UserDTO?>(null)
    override val userInfo: StateFlow<UserDTO?> = _userInfo.asStateFlow()

    override fun fetchUser() {
        coroutineScope.launch {
            val userResponse = apiClient.usersTenantApi.getUser(userId)
            if (userResponse != null) {
                _userInfo.value = userResponse
            }
        }
    }

    private val _userSpecialPermissions = MutableStateFlow<List<PermissionWithKey>?>(null)
    override val userSpecialPermissions: StateFlow<List<PermissionWithKey>?> = _userSpecialPermissions.asStateFlow()

    override fun fetchUserSpecialPermissions() {
        println("Fetching user special permissions for user ID: $userId")
        coroutineScope.launch {
            val response = apiClient.usersT.getUserSpecialPermissionsWithKey(userId)
            when (response) {
                is dev.byjtech.erp.common.ApiResponse.Success -> {
                    _userSpecialPermissions.value = response.data.toList()
                }
                is dev.byjtech.erp.common.ApiResponse.Error -> {
                    _userSpecialPermissions.value = null
                }
            }
        }
    }

    private val _userRoles = MutableStateFlow<List<RoleDTO>?>(null)
    override val userRoles: StateFlow<List<RoleDTO>?> = _userRoles.asStateFlow()

    override fun fetchUserRoles() {
        println("Fetching user roles for user ID: $userId")
        coroutineScope.launch {
            val userRolesResponse = apiClient.coreAuth.userRoles(userId)
            _userRoles.value = userRolesResponse.roles
        }
    }

    override fun deleteSpecialPermission(permissionId: String) {
        coroutineScope.launch {
            val response = apiClient.rolesT.deleteUserPermission(userId, permissionId)
            when (response) {
                is dev.byjtech.erp.common.ApiResponse.Success -> {
                    fetchUserSpecialPermissions()
                }
                is dev.byjtech.erp.common.ApiResponse.Error -> {
                    // Handle error
                }
            }
        }
    }

    override fun deleteRole(roleId: String) {
        coroutineScope.launch {
            println("Deleting role with ID: $roleId")
            val response = apiClient.rolesT.deleteUserRole(userId, roleId)
            when (response) {
                is dev.byjtech.erp.common.ApiResponse.Success -> {
                    fetchUserRoles()
                }
                is dev.byjtech.erp.common.ApiResponse.Error -> {
                    // Handle error
                }
            }
        }
    }

    init {
        coroutineScope.launch {
            fetchUser()
            fetchUserSpecialPermissions()
            fetchUserRoles()
        }
    }

}