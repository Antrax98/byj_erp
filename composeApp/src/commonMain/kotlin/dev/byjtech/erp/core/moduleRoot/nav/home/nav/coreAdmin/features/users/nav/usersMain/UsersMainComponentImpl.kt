package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.usersMain

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.UsersFeatureComponentImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsersMainComponentImpl(
    val componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val navTo: (UsersFeatureComponentImpl.Config) -> Unit
): UsersMainComponent, ComponentContext by componentContext {

    override val requiredPermissions: Set<PermissionKey> =
        setOf(
            CoreDefinition.Admin.All.key,
            CoreDefinition.Users.View.key
        )
    override val optionalPermissions: Set<PermissionKey> = setOf(
        CoreDefinition.Users.Update.key
    )

    private val coroutineScope = componentContext.coroutineScope()

    private val _state = MutableStateFlow(UsersMainState())
    override val state: StateFlow<UsersMainState> = _state

    private val _usersList = MutableStateFlow<List<UserDTO>>(emptyList())
    override val usersList: StateFlow<List<UserDTO>?> = _usersList


    override suspend fun loadUsers() {
        val usersResponse = apiClient.usersTenantApi.getCompanyUsers()
        if (usersResponse != null) {
            _usersList.value = usersResponse.users
        }
    }

    override fun updateIsLoading(isLoading: Boolean){
        _state.value = _state.value.copy(isLoading = isLoading)
    }

    init {
        updateIsLoading(true)
        coroutineScope.launch {
            loadUsers()
            updateIsLoading(false)
        }

    }
}