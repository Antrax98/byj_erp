package dev.byjtech.erp.modules.machinery.edit

import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.modules.machinery.dto.MachineryDTO
import kotlinx.coroutines.flow.StateFlow

interface MachineryEditComponent : FeatureComponent {
    val state: StateFlow<MachineryEditState>
    
    fun updateName(name: String)
    fun updateDescription(description: String)
    fun updateBrand(brand: String)
    fun updateModel(model: String)
    fun updateYear(year: String)
    fun updateSerialNumber(serialNumber: String)
    fun updateLicensePlate(licensePlate: String)
    fun updateStatus(status: String)
    fun updateLocation(location: String)
    
    fun saveMachinery()
    fun clearMessages()
    fun goBack()
}

data class MachineryEditState(
    val machinery: MachineryDTO? = null,
    val name: String = "",
    val description: String = "",
    val brand: String = "",
    val model: String = "",
    val year: String = "",
    val serialNumber: String = "",
    val licensePlate: String = "",
    val status: String = "",
    val location: String = "",
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
