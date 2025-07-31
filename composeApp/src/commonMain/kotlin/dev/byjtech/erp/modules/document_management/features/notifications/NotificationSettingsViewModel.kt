package dev.byjtech.erp.modules.document_management.features.notifications

import dev.byjtech.erp.document_management.request.UpdateNotificationSettingsRequest
import dev.byjtech.erp.modules.document_management.api.DocumentManagementClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NotificationSettingsViewModel(
    private val client: DocumentManagementClient,
    private val scope: CoroutineScope
) {

    private val _uiState = MutableStateFlow(NotificationSettingsUiState())
    val uiState: StateFlow<NotificationSettingsUiState> = _uiState.asStateFlow()

    fun loadSettings() {
        scope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val settings = client.getNotificationSettings()
                if (settings != null) {
                    _uiState.value = _uiState.value.copy(
                        settings = settings,
                        daysBeforeExpiration = settings.daysBeforeExpiration,
                        emailEnabled = settings.emailEnabled,
                        systemEnabled = settings.systemEnabled,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No se pudieron cargar las configuraciones"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar configuraciones: ${e.message}"
                )
            }
        }
    }

    fun updateSettings(request: UpdateNotificationSettingsRequest) {
        scope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val updatedSettings = client.updateNotificationSettings(request)
                if (updatedSettings != null) {
                    _uiState.value = _uiState.value.copy(
                        settings = updatedSettings,
                        daysBeforeExpiration = updatedSettings.daysBeforeExpiration,
                        emailEnabled = updatedSettings.emailEnabled,
                        systemEnabled = updatedSettings.systemEnabled,
                        isLoading = false,
                        successMessage = "Configuración guardada exitosamente"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No se pudo guardar la configuración"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al guardar configuración: ${e.message}"
                )
            }
        }
    }

    fun onDaysChanged(days: Int) {
        _uiState.value = _uiState.value.copy(daysBeforeExpiration = days)
    }

    fun onEmailEnabledChanged(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(emailEnabled = enabled)
    }

    fun onSystemEnabledChanged(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(systemEnabled = enabled)
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun dismissSuccess() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
}
