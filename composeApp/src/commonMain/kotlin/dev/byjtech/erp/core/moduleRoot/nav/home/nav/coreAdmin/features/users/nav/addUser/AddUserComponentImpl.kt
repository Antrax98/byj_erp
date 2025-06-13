package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.addUser

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany.TextFieldState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddUserComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val onFinished: (added: Boolean) -> Unit
): AddUserComponent, ComponentContext by componentContext {
    override val requiredPermissions: Set<PermissionKey> = setOf()

    private val _username = MutableStateFlow(TextFieldState())
    override val username: StateFlow<TextFieldState> = _username

    private val _email = MutableStateFlow(TextFieldState())
    override val email: StateFlow<TextFieldState> = _email

    override fun onUsernameChange(value: String) {
        _username.value = TextFieldState(value = value)
    }

    override fun onEmailChange(value: String) {
        _email.value = TextFieldState(value = value)
    }

    override fun onSubmitted() {
        val username = username.value.value
        val email = email.value.value

        var canSubmit = true

        if (username.isBlank()) {
            _username.value = TextFieldState(error = "Username is required")
            canSubmit = false
        }
        if (email.isBlank()) {
            _email.value = TextFieldState(error = "Email is required")
            canSubmit = false
        } else if (!isValidEmail(email)) {
            _email.value = TextFieldState(error = "Invalid email")
            canSubmit = false
        } else if (!isValidGmail(email)) {
            _email.value = TextFieldState(error = "must be a gmail account")
            canSubmit = false
        }

        if (canSubmit) {
            onFinished(true)
        } else{
            println("datos incorrectos")
        }

    }

    //TODO: mejorar esta validacion
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex(
            "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        )
        return emailRegex.matches(email)
    }

    //solo por ahora
    private fun isValidGmail(email: String): Boolean {
        val gmailRegex = Regex("^[A-Za-z0-9._%+-]+@gmail\\.com$")
        return gmailRegex.matches(email)
    }

}