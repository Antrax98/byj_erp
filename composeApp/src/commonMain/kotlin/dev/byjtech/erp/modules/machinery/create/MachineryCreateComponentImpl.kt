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
        clearMessagesIfNeeded()
    }

    override fun onNameChange(value: String) {
        _name.value = value
        clearMessagesIfNeeded()
    }

    override fun onDescriptionChange(value: String) {
        _description.value = value
        clearMessagesIfNeeded()
    }

    override fun onBrandChange(value: String) {
        _brand.value = value
        clearMessagesIfNeeded()
    }

    override fun onModelChange(value: String) {
        _model.value = value
        clearMessagesIfNeeded()
    }

    override fun onYearChange(value: String) {
        _year.value = value
        clearMessagesIfNeeded()
    }

    override fun onSerialNumberChange(value: String) {
        _serialNumber.value = value
        clearMessagesIfNeeded()
    }

    override fun onLicensePlateChange(value: String) {
        _licensePlate.value = value
        clearMessagesIfNeeded()
    }

    override fun onLocationChange(value: String) {
        _location.value = value
        clearMessagesIfNeeded()
    }

    override fun onSave() {
        if (!validateForm()) return

        coroutineScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, successMessage = null)

            try {
                // Obtener el company ID del usuario autenticado
                val currentUser = apiClient.coreAuth.getMe()
                val companyId = currentUser.companyId
                
                if (companyId == null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "No se pudo obtener la información de la compañía del usuario",
                        successMessage = null
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
                    onSuccess = { machinery ->
                        println("Maquinaria creada exitosamente: ${machinery.name}")
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            successMessage = "¡Maquinaria '${machinery.name}' creada exitosamente!",
                            error = null
                        )
                        // Navegar después de un delay para mostrar el mensaje
                        kotlinx.coroutines.delay(3000)
                        onMachineryCreated()
                    },
                    onFailure = { error ->
                        println("Error al crear maquinaria: ${error.message}")
                        val errorMessage = when {
                            error.message?.contains("duplicate", ignoreCase = true) == true -> 
                                "Ya existe una maquinaria con ese código"
                            error.message?.contains("network", ignoreCase = true) == true -> 
                                "Error de conexión. Verifique su conexión a internet"
                            error.message?.contains("unauthorized", ignoreCase = true) == true -> 
                                "No tiene permisos para crear maquinarias"
                            error.message?.contains("validation", ignoreCase = true) == true -> 
                                "Datos inválidos. Verifique los campos requeridos"
                            else -> "Error al crear la maquinaria: ${error.message ?: "Error desconocido"}"
                        }
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = errorMessage,
                            successMessage = null
                        )
                    }
                )
            } catch (e: Exception) {
                println("Excepción capturada: ${e.message}")
                e.printStackTrace()
                val errorMessage = when (e) {
                    is java.net.ConnectException -> "Error de conexión con el servidor"
                    is java.net.UnknownHostException -> "No se pudo conectar al servidor"
                    is kotlinx.coroutines.CancellationException -> "Operación cancelada"
                    else -> "Error inesperado: ${e.message ?: "Error desconocido"}"
                }
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = errorMessage,
                    successMessage = null
                )
            }
        }
    }

    override fun onCancel() {
        toHome()
    }

    override fun clearMessages() {
        _state.value = _state.value.copy(
            error = null,
            successMessage = null
        )
    }

    override fun onBack(): Boolean {
        return false // Let parent handle
    }

    private fun validateForm(): Boolean {
        // Limpiar mensajes anteriores antes de validar
        if (_state.value.successMessage != null) {
            _state.value = _state.value.copy(successMessage = null)
        }
        
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
            val errorMsg = errors.joinToString(", ")
            println("Errores de validación: $errorMsg")
            _state.value = _state.value.copy(
                error = errorMsg,
                successMessage = null
            )
            return false
        }

        return true
    }

    private fun clearMessagesIfNeeded() {
        if (_state.value.error != null || _state.value.successMessage != null) {
            clearMessages()
        }
    }
}
