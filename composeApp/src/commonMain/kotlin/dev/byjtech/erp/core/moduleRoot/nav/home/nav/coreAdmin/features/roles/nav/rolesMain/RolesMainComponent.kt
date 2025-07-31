package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolesMain

import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.common.PermissionAwareComponent
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.RoleDTO
import kotlinx.coroutines.flow.StateFlow
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.RolesFeatureComponentImpl.Config

interface RolesMainComponent: PermissionAwareComponent {
    val rolesState: StateFlow<Set<RoleDTO>?>
    val isLoading: StateFlow<Boolean>
    fun updateIsLoading(isLoading: Boolean)
    fun fetchAllRoles()
    val apiClient: ApiClient
    val navTo: (Config) -> Unit
    val navToRolePage: (roleId: String) -> Unit
}