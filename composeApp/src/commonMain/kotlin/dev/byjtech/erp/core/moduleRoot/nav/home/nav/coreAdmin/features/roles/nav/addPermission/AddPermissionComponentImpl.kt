package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addPermission

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.ModuleDTO
import dev.byjtech.erp.core.request.AssignPermissionRoleRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddPermissionComponentImpl(
    componentContext: ComponentContext,
    override val apiClient: ApiClient,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val onFinished: (added: Boolean) -> Unit,
    override val actModules: Set<ModuleDTO>,
    override val roleId: String
): AddPermissionComponent, ComponentContext by componentContext {

    val coroutineScope = componentContext.coroutineScope()

    private val _possiblePermissions = MutableStateFlow<Map<String,Set<PermissionWithKey>>>(emptyMap())
    override val possiblePermissions: StateFlow<Map<String,Set<PermissionWithKey>>> = _possiblePermissions

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    private val _isBusy = MutableStateFlow(false)
    override val isBusy: StateFlow<Boolean> = _isBusy

    override val actPermissions: Set<PermissionKey> = userPermissions.value //son los mismos que userPermissions pero no son StateFlow


    override fun addPermission(permission: PermissionKey) {
        _isBusy.value = true
        val requestData = AssignPermissionRoleRequest(
            roleId = roleId,
            permissionKeys = setOf(permission)
        )
        coroutineScope.launch {
            val response = apiClient.rolesT.assignPermissionsToRole(requestData)
            when (response) {
                is dev.byjtech.erp.common.ApiResponse.Success -> {
                    println("permiso agregado")
                    onFinished(true)
                }
                is dev.byjtech.erp.common.ApiResponse.Error -> {
                    //TODO(): mostrar error como popup
                }
            }
            _isBusy.value = false
        }
    }

    override suspend fun loadPermissions() {
        _isLoading.value = true
        coroutineScope.launch {
            val moduleIdsSet = actModules.map { it.id }.toSet()
            val response = apiClient.rolesT.getModulesPermissionKeys(moduleIdsSet)
            println(response)
            if (response is dev.byjtech.erp.common.ApiResponse.Success) {
                val firstData = response.data
                val secondResponse = apiClient.rolesT.getPermissionsByRoleId(roleId)
                var permData: Set<PermissionWithKey> = emptySet()
                var updatedData = emptySet<PermissionWithKey>()
                if (secondResponse is dev.byjtech.erp.common.ApiResponse.Success) {
                    permData = permData.plus(secondResponse.data)
                    println("secondResponse: ${secondResponse.data}")
                    if (firstData.isNotEmpty()) {
                        updatedData = firstData.map { permission ->
                            if (permData.contains(permission)) {
                                permission.copy(permission = permission.permission.copy(id = ""))
                            } else {
                                permission
                            }
                        }.toSet()
                    }
                }
                if (updatedData.isNotEmpty()) {
                    _possiblePermissions.value = updatedData.groupBy { it.key.module }.mapValues { (_, categoryList) ->
                        categoryList.toSet()
                    }
                } else {
                    _possiblePermissions.value = response.data.groupBy { it.key.module }.mapValues { (_, list) -> list.toSet() }
                }
                println(_possiblePermissions.value)
            }
            _isLoading.value = false
        }
    }

    init {
        coroutineScope.launch {
            loadPermissions()
        }
    }

}