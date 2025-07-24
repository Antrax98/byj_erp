package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.assignSpecialPermission

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.ModuleDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AssignSpecialPermissionComponentImpl(
    componentContext: ComponentContext,
    override val apiClient: ApiClient,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val actModules: Set<ModuleDTO>,
    override val userIdToAssign: String,
    override val onFinished: (added: Boolean) -> Unit
): AssignSpecialPermissionComponent, ComponentContext by componentContext {

    val coroutineScope = componentContext.coroutineScope()

    private val _possiblePermissions = MutableStateFlow<Map<String,Set<PermissionWithKey>>>(emptyMap())
    override val possiblePermissions: StateFlow<Map<String,Set<PermissionWithKey>>> = _possiblePermissions

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    private val _isBusy = MutableStateFlow(false)
    override val isBusy: StateFlow<Boolean> = _isBusy

    override val actPermissions: Set<PermissionKey> = userPermissions.value

    override fun addPermission(permission: PermissionKey) {
        _isBusy.value = true
        coroutineScope.launch {
            val response = apiClient.usersT.assignSpecialPermission(userIdToAssign, permission)
            if (response is dev.byjtech.erp.common.ApiResponse.Error) {
                onFinished(true) //true por ahora
            }
            else{
                onFinished(true)
            }
            _isBusy.value = false
        }
    }

    override fun loadPermissions() {
        _isLoading.value = true
        coroutineScope.launch {
            val moduleIdsSet = actModules.map { it.id }.toSet()
            println(moduleIdsSet)
            val response = apiClient.rolesT.getModulesPermissionKeys(moduleIdsSet)
            println(response)
            if (response is dev.byjtech.erp.common.ApiResponse.Success) {
                val firstData = response.data
                val secondResponse = apiClient.usersT.getUserSpecialPermissionsWithKey(userIdToAssign)
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
        loadPermissions()
    }
}