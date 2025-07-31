package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addRole

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.RolesFeatureComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany.TextFieldState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import dev.byjtech.erp.core.dto.RoleDTO
import kotlinx.coroutines.launch

class AddRoleComponentImpl(
    val componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val navTo: (RolesFeatureComponentImpl.Config) -> Unit, //porsiacaso
    override val onFinished: (added: Boolean) -> Unit
): AddRoleComponent, ComponentContext by componentContext {

    val coroutineScope = componentContext.coroutineScope()

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    private val _isBusy = MutableStateFlow(false)
    override val isBusy: StateFlow<Boolean> = _isBusy

    private val _name = MutableStateFlow(TextFieldState())
    override val name: StateFlow<TextFieldState> = _name

    private val _description = MutableStateFlow(TextFieldState())
    override val description: StateFlow<TextFieldState> = _description

    override fun onNameChange(value: String) {
        _name.value = _name.value.copy(value = value)
    }

    override fun onDescriptionChange(value: String) {
        _description.value = _description.value.copy(value = value)
    }

    override fun onSubmitted() {

        val name = name.value.value
        val description = description.value.value

        var canSubmit = true

        if (name.isBlank()) {
            _name.value = _name.value.copy(error = "Name is required")
            canSubmit = false
        }
        if (description.isBlank()) {
            _description.value = _description.value.copy(error = "Description is required")
            canSubmit = false
        }

        _isBusy.value = true

        if (canSubmit) {
            val newRole = RoleDTO(
                name = name,
                description = description
            )
            coroutineScope.launch {
                val response = apiClient.rolesT.createRole(newRole)
                when(response){
                    is ApiResponse.Success -> {
                        onFinished(true)
                    }
                    is ApiResponse.Error -> {
                        println("error: ${response.data}")
                        //todo(): saltar un popup o algo explicando que paso para:
                        //"UNKNOWN_ERROR"
                        //"NETWORK_ERROR"
                        //etc...
                    }
                }
                _isBusy.value = false
            }

        } else {
            println("datos incorrectos")
        }
    }

}