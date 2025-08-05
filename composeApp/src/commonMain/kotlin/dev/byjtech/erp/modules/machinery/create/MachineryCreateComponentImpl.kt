package dev.byjtech.erp.modules.machinery.create

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.modules.machinery.MachineryDefinition
import dev.byjtech.erp.modules.machinery.api.MachineryApiImpl
import dev.byjtech.erp.modules.machinery.request.CreateMachineryRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MachineryCreateComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val toHome: () -> Unit,
    override val updateTitle: (newTitle: String) -> Unit,
    private val onMachineryCreated: () -> Unit
) : MachineryCreateComponent, ComponentContext by componentContext {

    private val coroutineScope = componentContext.coroutineScope()
    private val machineryApi = MachineryApiImpl(apiClient)

    private val _state = MutableStateFlow(MachineryCreateState())
    override val state: StateFlow<MachineryCreateState> = _state.asStateFlow()

    // Form fields
    private val _code = MutableStateFlow("")
    override val code: StateFlow<String> = _code.asStateFlow()

    private val _name = MutableStateFlow("")
    override val name: StateFlow<String> = _name.asStateFlow()

    private val _description = MutableStateFlow("")
    override val description: StateFlow<String> = _description.asStateFlow()

    private val _brand = MutableStateFlow("")
    override val brand: StateFlow<String> = _brand.asStateFlow()

    private val _model = MutableStateFlow("")
    override val model: StateFlow<String> = _model.asStateFlow()

    private val _year = MutableStateFlow("")
    override val year: StateFlow<String> = _year.asStateFlow()

    private val _serialNumber = MutableStateFlow("")
    override val serialNumber: StateFlow<String> = _serialNumber.asStateFlow()

    private val _licensePlate = MutableStateFlow("")
    override val licensePlate: StateFlow<String> = _licensePlate.asStateFlow()

    private val _location = MutableStateFlow("")
    override val location: StateFlow<String> = _location.asStateFlow()

    init {
        updateTitle("Crear Maquinaria")
    }

    override fun onCodeChange(value: String) {
        _code.value = value
    }

    override fun onNameChange(value: String) {
        _name.value = value
    }

    override fun onDescriptionChange(value: String) {
        _description.value = value
    }

    override fun onBrandChange(value: String) {
        _brand.value = value
    }

    override fun onModelChange(value: String) {
        _model.value = value
    }

    override fun onYearChange(value: String) {
        _year.value = value
    }

    override fun onSerialNumberChange(value: String) {
        _serialNumber.value = value
    }

    override fun onLicensePlateChange(value: String) {
        _licensePlate.value = value
    }

    override fun onLocationChange(value: String) {
        _location.value = value
    }

    override fun onSave() {
        if (!validateForm()) return

        coroutineScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                // Obtener el company ID del usuario autenticado
                val currentUser = apiClient.coreAuth.getMe()
                val companyId = currentUser.companyId
                
                if (companyId == null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "No se pudo obtener la información de la compañía del usuario"
                    )
                    return@launch
                }

                val request = CreateMachineryRequest(
                    code = _code.value.trim(),
                    name = _name.value.trim(),
                    description = _description.value.trim().takeIf { it.isNotEmpty() },
                    brand = _brand.value.trim(),
                    model = _model.value.trim(),
                    year = _year.value.trim().toIntOrNull(),
                    serialNumber = _serialNumber.value.trim().takeIf { it.isNotEmpty() },
                    licensePlate = _licensePlate.value.trim().takeIf { it.isNotEmpty() },
                    status = "ACTIVE",
                    location = _location.value.trim().takeIf { it.isNotEmpty() },
                    companyId = companyId
                )

                val result = machineryApi.createMachinery(request)
                result.fold(
                    onSuccess = {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                        onMachineryCreated()
                    },
                    onFailure = { error ->
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = error.message ?: "Error al crear la maquinaria"
                        )
                    }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error inesperado"
                )
            }
        }
    }

    override fun onCancel() {
        toHome()
    }

    override fun onBack(): Boolean {
        return false // Let parent handle
    }

    private fun validateForm(): Boolean {
        val errors = mutableListOf<String>()

        if (_code.value.trim().isEmpty()) {
            errors.add("El código es requerido")
        }
        if (_name.value.trim().isEmpty()) {
            errors.add("El nombre es requerido")
        }
        if (_brand.value.trim().isEmpty()) {
            errors.add("La marca es requerida")
        }
        if (_model.value.trim().isEmpty()) {
            errors.add("El modelo es requerido")
        }

        if (errors.isNotEmpty()) {
            _state.value = _state.value.copy(
                error = errors.joinToString(", ")
            )
            return false
        }

        return true
    }
}
