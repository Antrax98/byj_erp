package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.addUser

import dev.byjtech.erp.common.PermissionAwareComponent
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany.TextFieldState
import kotlinx.coroutines.flow.StateFlow

interface AddUserComponent: PermissionAwareComponent {
    val username: StateFlow<TextFieldState>
    val email: StateFlow<TextFieldState>
    val onFinished: (added: Boolean) -> Unit
    fun onUsernameChange(value: String)
    fun onEmailChange(value: String)
    fun onSubmitted()
}