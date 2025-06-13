package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany

import dev.byjtech.erp.common.api.ApiClient
import kotlinx.coroutines.flow.StateFlow

interface AddCompanyComponent {
    val apiClient: ApiClient
    val onFinished: (added: Boolean) -> Unit
    val nameState: StateFlow<TextFieldState>
    val contactEmailState: StateFlow<TextFieldState>
    val companyAdminEmailState: StateFlow<TextFieldState>
    fun onNameChanged(value: String)
    fun onContactEmailChanged(value: String)
    fun onCompanyAdminEmailChanged(value: String)
    fun onSubmitted()
}