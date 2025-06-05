package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.usersMain

import dev.byjtech.erp.common.PermissionAwareComponent
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.UsersFeatureComponentImpl
import kotlinx.coroutines.flow.StateFlow

interface UsersMainComponent: PermissionAwareComponent {
    val state: StateFlow<UsersMainState>
    val usersList: StateFlow<List<UserDTO>?>
    suspend fun loadUsers()
    fun updateIsLoading(isLoading: Boolean)
    val apiClient: ApiClient
    val navTo: (UsersFeatureComponentImpl.Config) -> Unit
}