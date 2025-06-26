package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addRole

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.RolesFeatureComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany.TextFieldState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddRoleComponentImpl(
    val componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val navTo: (RolesFeatureComponentImpl.Config) -> Unit //porsiacaso
): AddRoleComponent, ComponentContext by componentContext {

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
        //TODO: implementar
    }

}