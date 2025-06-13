package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.api.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddCompanyComponentImpl(
    componentContext: ComponentContext,
    override val apiClient: ApiClient,
    override val onFinished: (added: Boolean) -> Unit
): AddCompanyComponent, ComponentContext by componentContext {
    private val _nameState = MutableStateFlow(TextFieldState())
    override val nameState: StateFlow<TextFieldState> = _nameState

    private val _contactEmailState = MutableStateFlow(TextFieldState())
    override val contactEmailState: StateFlow<TextFieldState> = _contactEmailState

    private val _companyAdminEmailState = MutableStateFlow(TextFieldState())
    override val companyAdminEmailState: StateFlow<TextFieldState> = _companyAdminEmailState

    override fun onNameChanged(value: String) {
        _nameState.value = _nameState.value.copy(value = value)
    }
    override fun onContactEmailChanged(value: String) {
        _contactEmailState.value = _contactEmailState.value.copy(value = value)
    }
    override fun onCompanyAdminEmailChanged(value: String) {
        _companyAdminEmailState.value = _companyAdminEmailState.value.copy(value = value)
    }
    override fun onSubmitted() {
        println("onSubmitted triggered")
        val name = _nameState.value.value
        val contactEmail = _contactEmailState.value.value
        val companyAdminEmail = _companyAdminEmailState.value.value

        _nameState.value = _nameState.value.copy(error = null)
        _contactEmailState.value = _contactEmailState.value.copy(error = null)
        _companyAdminEmailState.value = _companyAdminEmailState.value.copy(error = null)

        var isValid = true
        //TODO: separar cada validacion en un metodo separado
        //validar nombre
        if (name.isBlank()) {
            _nameState.value = _nameState.value.copy(error = "Name is required")
            isValid = false
        }
        //validar email de contacto
        if (contactEmail.isBlank()) {
            _contactEmailState.value = _contactEmailState.value.copy(error = "Contact email is required")
            isValid = false
        } else if (!isValidEmail(contactEmail)) {
            _contactEmailState.value = _contactEmailState.value.copy(error = "Invalid contact email")
            isValid = false
        }
        //validar email de admin de empresa
        if (companyAdminEmail.isBlank()) {
            _companyAdminEmailState.value = _companyAdminEmailState.value.copy(error = "Company admin email is required")
            isValid = false
        } else if (!isValidEmail(companyAdminEmail)) {
            _companyAdminEmailState.value = _companyAdminEmailState.value.copy(error = "Invalid company admin email")
            isValid = false
        } else if (!isValidGmail(companyAdminEmail)) {
            _companyAdminEmailState.value = _companyAdminEmailState.value.copy(error = "must be a gmail account")
            isValid = false
        }

        if (isValid) {
            //todo crear empresa
            println("onSubmitted triggered")
            println("name: $name")
            println("contactEmail: $contactEmail")
            println("companyAdminEmail: $companyAdminEmail")
            onFinished(true)

        }else{
            println("onSubmitted triggered but not valid")
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