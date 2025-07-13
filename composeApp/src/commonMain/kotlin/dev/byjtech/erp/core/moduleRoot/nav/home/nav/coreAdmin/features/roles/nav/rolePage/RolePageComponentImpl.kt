package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolePage

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.RolesFeatureComponentImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RolePageComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val roleId: String,
    override val apiClient: ApiClient,
    override val navTo: (RolesFeatureComponentImpl.Config) -> Unit
): RolePageComponent, ComponentContext by componentContext {

    private val coroutineScope = componentContext.coroutineScope()

    private val _roleInfo = MutableStateFlow<RoleDTO?>(null)
    override val roleInfo: StateFlow<RoleDTO?> = _roleInfo

    private val _rolePermissions = MutableStateFlow<Set<PermissionWithKey>>(emptySet())
    override val rolePermissions: StateFlow<Set<PermissionWithKey>> = _rolePermissions

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    override fun fetchRole() {
        coroutineScope.launch {
            val roleResponse = apiClient.rolesT.getRoleById(roleId)
            if (roleResponse is ApiResponse.Success) {
                _roleInfo.value = roleResponse.data
            } else if (roleResponse is ApiResponse.Error) {
                _roleInfo.value = null
            }
        }
    }
    override fun fetchRolePermissions() {
        coroutineScope.launch {
            val permissionsResponse = apiClient.rolesT.getPermissionsByRoleId(roleId)
            if (permissionsResponse is ApiResponse.Success) {
                _rolePermissions.value = permissionsResponse.data
                println("Role permissions: ${permissionsResponse.data}")
            } else if (permissionsResponse is ApiResponse.Error) {
                _rolePermissions.value = emptySet()
                println("Error fetching role permissions: ${permissionsResponse.code}")
            }
        }
    }

    override fun deletePermission(permissionId: String) {
        coroutineScope.launch {
            val response = apiClient.rolesT.deleteRolePermission(roleId, permissionId)
            if (response is ApiResponse.Success) {
                fetchRolePermissions()
            } else if (response is ApiResponse.Error) {
                println("Error deleting role permission: ${response.code}")
            }
        }
    }

    init {
        coroutineScope.launch {
            _isLoading.value = true
            fetchRole()
            fetchRolePermissions()
            _isLoading.value = false
        }
    }
}