package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.assignRole

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.request.AssignRoleRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AssignRoleComponentImpl(
    componentContext: ComponentContext,
    override val apiClient: ApiClient,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val userIdToAssign: String,
    override val actUserRoles: Set<RoleDTO>,
    override val onFinished: (assigned: Boolean) -> Unit
): AssignRoleComponent, ComponentContext by componentContext {
    override val requiredPermissions: Set<PermissionKey> = setOf(
        CoreDefinition.Roles.Assign.key,
        CoreDefinition.Admin.All.key
    )

    val coroutineScope = componentContext.coroutineScope()

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    private val _isBusy = MutableStateFlow(false)
    override val isBusy: StateFlow<Boolean> = _isBusy

    private val _assignableRoles = MutableStateFlow<Set<RoleDTO>>(emptySet())
    override val assignableRoles: StateFlow<Set<RoleDTO>> = _assignableRoles

    override fun fetchAssignableRoles() {
        _isLoading.value = true
        coroutineScope.launch {
            val response = apiClient.rolesT.getAllRoles()
            when(response){
                is ApiResponse.Success -> {
                    val assignableRoles = response.data.filter { role ->
                        role.id !in actUserRoles.map { it.id }
                    }.toSet()
                    _assignableRoles.value = assignableRoles
                }
                is ApiResponse.Error -> {
                    println("Error fetching assignable roles: ${response.code}")
                    _assignableRoles.value = emptySet()
                }

            }
            _isLoading.value = false
        }

    }

    override fun assignRole(roleId: String) {
        _isBusy.value = true
        coroutineScope.launch {
            val data = AssignRoleRequest(userIdToAssign, roleId)
            val response = apiClient.rolesT.assignRoleToUser(data)
            when(response){
                is ApiResponse.Success -> {
                    onFinished(true)
                }
                is ApiResponse.Error -> {
                    println("Error assigning role: ${response.code}")
                    onFinished(false)
                }
            }
            _isBusy.value = false
        }
    }



    init {
        fetchAssignableRoles()
    }
}