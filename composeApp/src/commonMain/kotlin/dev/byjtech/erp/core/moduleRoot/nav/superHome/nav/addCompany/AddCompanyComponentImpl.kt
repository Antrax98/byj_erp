package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.request.CreateCompanyRequest
import dev.byjtech.erp.core.response.ErrorList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddCompanyComponentImpl(
    componentContext: ComponentContext,
    override val apiClient: ApiClient,
    override val onFinished: (added: Boolean) -> Unit
): AddCompanyComponent, ComponentContext by componentContext {

    private val coroutineScope = componentContext.coroutineScope()

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    private val _nameState = MutableStateFlow(TextFieldState())
    override val nameState: StateFlow<TextFieldState> = _nameState

    private val _rutState = MutableStateFlow(TextFieldState())
    override val rutState: StateFlow<TextFieldState> = _rutState

    private val _contactEmailState = MutableStateFlow(TextFieldState())
    override val contactEmailState: StateFlow<TextFieldState> = _contactEmailState

    private val _companyAdminEmailState = MutableStateFlow(TextFieldState())
    override val companyAdminEmailState: StateFlow<TextFieldState> = _companyAdminEmailState

    private val _adminNameState = MutableStateFlow(TextFieldState())
    override val adminNameState: StateFlow<TextFieldState> = _adminNameState

    override fun onNameChanged(value: String) {
        _nameState.value = _nameState.value.copy(value = value)
    }
    override fun onRutChanged(value: String) {
        _rutState.value = _rutState.value.copy(value = value)
    }
    override fun onContactEmailChanged(value: String) {
        _contactEmailState.value = _contactEmailState.value.copy(value = value)
    }
    override fun onCompanyAdminEmailChanged(value: String) {
        _companyAdminEmailState.value = _companyAdminEmailState.value.copy(value = value)
    }
    override fun onAdminNameChanged(value: String) {
        _adminNameState.value = _adminNameState.value.copy(value = value)
    }


    override fun onSubmitted() {
        println("onSubmitted triggered")
        val name = _nameState.value.value
        val rut = _rutState.value.value
        val contactEmail = _contactEmailState.value.value
        val companyAdminEmail = _companyAdminEmailState.value.value
        val adminName = _adminNameState.value.value

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
        //validar nombre del admin
        if (adminName.isBlank()) {
            _adminNameState.value = _adminNameState.value.copy(error = "Admin name is required")
            isValid = false
        }

        if (isValid) {
            //todo crear empresa
            println("onSubmitted triggered")
            println("name: $name")
            println("rut: $rut")
            println("contactEmail: $contactEmail")
            println("companyAdminEmail: $companyAdminEmail")

            //TODO(): areglar un poco los datos que se mandan

            val requestData = CreateCompanyRequest(
                adminEmail = companyAdminEmail,
                adminName = adminName,
                companyName = name,
                companyRut = rut,
                companyContactEmail = contactEmail
            )

            //TODO(): bloquear el boton de enviar hasta que se complete la peticion

            _isLoading.value = true
            coroutineScope.launch {
                val response = apiClient.companiesSA.createCompany(requestData)
                println("response: $response")
                _isLoading.value = false
                when(response){
                    is ApiResponse.Success -> {
                        onFinished(true)
                    }
                    is ApiResponse.Error -> {
                        when(response.code){
                            "CONFLICT_REQUEST" -> {
                                val errorList = response.data as ErrorList
                                errorList.errors.forEach {
                                    if(it == "COMPANY"){
                                        _rutState.value = _rutState.value.copy(error = "Compañia con el mismo Rut ya existe")
                                    } else if(it == "USER"){
                                        _companyAdminEmailState.value = _companyAdminEmailState.value.copy(error = "Usuario con el mismo email ya existe")
                                    }
                                }
                            }
                            else -> {
                                //todo(): saltar un popup o algo explicando que paso para:
                                //"UNKNOWN_ERROR"
                                //"NETWORK_ERROR"
                                //"BAD_REQUEST"
                            }
                        }
                    }
                }
            }



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