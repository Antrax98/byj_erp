package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolesMain

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.RolesFeatureComponentImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RolesMainComponentImpl(
    val componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val navTo: (RolesFeatureComponentImpl.Config) -> Unit
): RolesMainComponent, ComponentContext by componentContext {

    override val requiredPermissions: Set<PermissionKey> = setOf(
        CoreDefinition.Roles.View.key
    )

    override val optionalPermissions: Set<PermissionKey> = setOf(
        CoreDefinition.Roles.Create.key,
        CoreDefinition.Roles.Update.key,
        CoreDefinition.Roles.Delete.key
    )

    private val coroutineScope = componentContext.coroutineScope()

    private val _rolesState: MutableStateFlow<Set<RoleDTO>?> = MutableStateFlow(null)
    override val rolesState: StateFlow<Set<RoleDTO>?> = _rolesState

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    //inecesario si no lo usa el Screen???
    override fun updateIsLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    override suspend fun fetchAllRoles() {
        when (val response = apiClient.rolesT.getAllRoles()) {
            is ApiResponse.Success -> {
                _rolesState.value = response.data
            }
            is ApiResponse.Error -> {
                _rolesState.value = null
                //enviar un pop up o snackbar?
            }
        }
    }

    init {
        coroutineScope.launch {
            _isLoading.value = true
            fetchAllRoles()
            _isLoading.value = false
        }
    }
}