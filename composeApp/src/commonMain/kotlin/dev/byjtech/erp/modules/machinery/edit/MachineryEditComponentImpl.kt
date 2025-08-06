package dev.byjtech.erp.modules.machinery.edit

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.modules.machinery.api.MachineryApi
import dev.byjtech.erp.modules.machinery.dto.MachineryDTO
import dev.byjtech.erp.modules.machinery.request.UpdateMachineryRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MachineryEditComponentImpl(
    componentContext: ComponentContext,
    private val machineryApi: MachineryApi,
    override val apiClient: ApiClient,
    private val machinery: MachineryDTO,
    override val toHome: () -> Unit,
    override val updateTitle: (String) -> Unit,
    private val onNavigateBack: () -> Unit
) : MachineryEditComponent, ComponentContext by componentContext {

    override val userPermissions: StateFlow<Set<PermissionKey>> = 
        MutableStateFlow<Set<PermissionKey>>(emptySet()).asStateFlow()

    override fun onBack(): Boolean {
        onNavigateBack()
        return true
    }

    private val _state = MutableStateFlow(
        MachineryEditState(
            machinery = machinery,
            name = machinery.name,
            description = machinery.description ?: "",
            brand = machinery.manufacturer ?: "",
            model = machinery.model ?: "",
            year = "",
            serialNumber = machinery.serialNumber ?: "",
            licensePlate = "",
            status = machinery.status,
            location = machinery.location ?: ""
        )
    )
    override val state: StateFlow<MachineryEditState> = _state.asStateFlow()

    override fun updateName(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    override fun updateDescription(description: String) {
        _state.value = _state.value.copy(description = description)
    }

    override fun updateBrand(brand: String) {
        _state.value = _state.value.copy(brand = brand)
    }

    override fun updateModel(model: String) {
        _state.value = _state.value.copy(model = model)
    }

    override fun updateYear(year: String) {
        _state.value = _state.value.copy(year = year)
    }

    override fun updateSerialNumber(serialNumber: String) {
        _state.value = _state.value.copy(serialNumber = serialNumber)
    }

    override fun updateLicensePlate(licensePlate: String) {
        _state.value = _state.value.copy(licensePlate = licensePlate)
    }

    override fun updateStatus(status: String) {
        _state.value = _state.value.copy(status = status)
    }

    override fun updateLocation(location: String) {
        _state.value = _state.value.copy(location = location)
    }

    override fun saveMachinery() {
        val currentState = _state.value
        
        if (currentState.name.isBlank()) {
            _state.value = currentState.copy(errorMessage = "El nombre es obligatorio")
            return
        }
        
        if (currentState.brand.isBlank()) {
            _state.value = currentState.copy(errorMessage = "La marca es obligatoria")
            return
        }
        
        if (currentState.model.isBlank()) {
            _state.value = currentState.copy(errorMessage = "El modelo es obligatorio")
            return
        }

        _state.value = currentState.copy(isLoading = true, errorMessage = null)

        coroutineScope().launch {
            try {
                val request = UpdateMachineryRequest(
                    name = currentState.name,
                    description = currentState.description.takeIf { it.isNotBlank() },
                    brand = currentState.brand,
                    model = currentState.model,
                    year = currentState.year.toIntOrNull(),
                    serialNumber = currentState.serialNumber.takeIf { it.isNotBlank() },
                    licensePlate = currentState.licensePlate.takeIf { it.isNotBlank() },
                    status = currentState.status,
                    location = currentState.location.takeIf { it.isNotBlank() }
                )

                machineryApi.updateMachinery(machinery.id, request)
                    .onSuccess {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            successMessage = "Maquinaria actualizada exitosamente"
                        )
                        // Navegar hacia atrás después de 2 segundos
                        coroutineScope().launch {
                            kotlinx.coroutines.delay(2000)
                            onNavigateBack()
                        }
                    }
                    .onFailure { error ->
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Error al actualizar la maquinaria"
                        )
                    }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Error inesperado: ${e.message}"
                )
            }
        }
    }

    override fun clearMessages() {
        _state.value = _state.value.copy(successMessage = null, errorMessage = null)
    }

    override fun goBack() {
        onNavigateBack()
    }
}
