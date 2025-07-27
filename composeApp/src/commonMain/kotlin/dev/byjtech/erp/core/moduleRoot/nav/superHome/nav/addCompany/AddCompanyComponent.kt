package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany

import dev.byjtech.erp.common.api.ApiClient
import kotlinx.coroutines.flow.StateFlow

interface AddCompanyComponent {
    val apiClient: ApiClient
    val onFinished: (added: Boolean) -> Unit
    val isLoading: StateFlow<Boolean>
    val nameState: StateFlow<TextFieldState>
    val rutState: StateFlow<TextFieldState>
    val contactEmailState: StateFlow<TextFieldState>
    val companyAdminEmailState: StateFlow<TextFieldState>
    val adminNameState: StateFlow<TextFieldState>
    val isBusy: StateFlow<Boolean>
    fun onNameChanged(value: String)
    fun onRutChanged(value: String)
    fun onContactEmailChanged(value: String)
    fun onCompanyAdminEmailChanged(value: String)
    fun onAdminNameChanged(value: String)
    fun onSubmitted()
}