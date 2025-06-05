package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.usersMain

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UsersMainComponentImpl(
    val componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient
): UsersMainComponent, ComponentContext by componentContext {
    private val _state = MutableStateFlow(UsersMainState())
    override val state: StateFlow<UsersMainState> = _state
}