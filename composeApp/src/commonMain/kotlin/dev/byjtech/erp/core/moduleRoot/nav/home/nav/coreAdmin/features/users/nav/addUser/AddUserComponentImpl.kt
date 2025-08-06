package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.addUser

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany.TextFieldState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddUserComponentImpl(
    componentContext: ComponentContext,
    override val apiClient: ApiClient,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val onFinished: (added: Boolean) -> Unit
): AddUserComponent, ComponentContext by componentContext {
    override val requiredPermissions: Set<PermissionKey> = setOf()

    private val _isBusy = MutableStateFlow(false)
    override val isBusy: StateFlow<Boolean> = _isBusy

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

    val coroutineScope = componentContext.coroutineScope()

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
        }

        if (canSubmit) {
            val newUser = UserDTO(
                name = username,
                email = email
            )

            coroutineScope.launch {
                val response = apiClient.usersT.createUser(newUser)
                when(response){
                    is ApiResponse.Success -> {
                        onFinished(true)
                    }
                    is ApiResponse.Error -> {
                        println("error: ${response.data}")
                        //todo(): saltar un popup o algo explicando que paso para:
                        //"UNKNOWN_ERROR"
                        //"NETWORK_ERROR"
                        //"BAD_REQUEST"
                    }
                }
            }
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

}