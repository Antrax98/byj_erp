package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addRole

import dev.byjtech.erp.common.PermissionAwareComponent
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.RolesFeatureComponentImpl.Config
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany.TextFieldState
import kotlinx.coroutines.flow.StateFlow

interface AddRoleComponent: PermissionAwareComponent {
    val apiClient: ApiClient
    val navTo: (Config) -> Unit //porsiacaso
    //estados para el formulario
    val name: StateFlow<TextFieldState>
    val description: StateFlow<TextFieldState>
    val isBusy: StateFlow<Boolean>
    val isLoading: StateFlow<Boolean>
    val onFinished: (added: Boolean) -> Unit
    fun onNameChange(value: String)
    fun onDescriptionChange(value: String)
    fun onSubmitted()

}