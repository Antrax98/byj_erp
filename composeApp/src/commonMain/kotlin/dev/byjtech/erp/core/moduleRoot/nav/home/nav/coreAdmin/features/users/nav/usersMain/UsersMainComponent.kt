package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.usersMain

import dev.byjtech.erp.common.PermissionAwareComponent
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import kotlinx.coroutines.flow.StateFlow

interface UsersMainComponent: PermissionAwareComponent {
    val state: StateFlow<UsersMainState>
    val userPermissions: StateFlow<Set<PermissionKey>>
    val apiClient: ApiClient
}