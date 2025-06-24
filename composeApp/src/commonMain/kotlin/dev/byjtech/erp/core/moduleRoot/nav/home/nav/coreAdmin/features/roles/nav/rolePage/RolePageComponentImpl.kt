package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolePage

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
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

    private val _rolePermissions = MutableStateFlow<List<PermissionWithKey>?>(null)
    override val rolePermissions: StateFlow<List<PermissionWithKey>?> = _rolePermissions

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    override suspend fun fetchRole() {
        TODO("Not yet implemented")
    }
    override suspend fun fetchRolePermissions() {
        TODO("Not yet implemented")
    }

    init {
        coroutineScope.launch {
            _isLoading.value = true
            //fetchRole()
            //fetchRolePermissions()
            _isLoading.value = false
        }
    }
}