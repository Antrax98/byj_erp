package dev.byjtech.erp.modules.machinery.create

import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.modules.machinery.dto.MachineryDTO
import kotlinx.coroutines.flow.StateFlow

interface MachineryCreateComponent : FeatureComponent {
    val state: StateFlow<MachineryCreateState>
    
    // Form fields
    val code: StateFlow<String>
    val name: StateFlow<String>
    val description: StateFlow<String>
    val brand: StateFlow<String>
    val model: StateFlow<String>
    val year: StateFlow<String>
    val serialNumber: StateFlow<String>
    val licensePlate: StateFlow<String>
    val location: StateFlow<String>
    
    // Actions
    fun onCodeChange(value: String)
    fun onNameChange(value: String)
    fun onDescriptionChange(value: String)
    fun onBrandChange(value: String)
    fun onModelChange(value: String)
    fun onYearChange(value: String)
    fun onSerialNumberChange(value: String)
    fun onLicensePlateChange(value: String)
    fun onLocationChange(value: String)
    
    fun onSave()
    fun onCancel()
    fun clearMessages()
}

data class MachineryCreateState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val isSuccess: Boolean = false
)
