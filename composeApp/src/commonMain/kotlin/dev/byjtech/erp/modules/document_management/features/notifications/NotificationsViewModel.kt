package dev.byjtech.erp.modules.document_management.features.notifications

import dev.byjtech.erp.modules.document_management.api.DocumentManagementClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val client: DocumentManagementClient,
    private val scope: CoroutineScope
) {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    private val _notificationCount = MutableStateFlow(0)
    val notificationCount: StateFlow<Int> = _notificationCount.asStateFlow()

    fun loadNotifications() {
        scope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val notifications = client.getAllNotifications()
                _uiState.value = _uiState.value.copy(
                    notifications = notifications,
                    isLoading = false
                )
                // También actualizar el contador
                loadNotificationCount()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar notificaciones: ${e.message}"
                )
            }
        }
    }

    fun loadNotificationCount() {
        scope.launch {
            try {
                val count = client.getNotificationCount()
                _notificationCount.value = count
            } catch (e: Exception) {
                // Si falla, mantener el contador actual
            }
        }
    }

    fun markAsRead(notificationId: String) {
        scope.launch {
            try {
                val updatedNotification = client.markNotificationAsRead(notificationId)
                if (updatedNotification != null) {
                    // Actualizar la lista local
                    val updatedList = _uiState.value.notifications.map { notification ->
                        if (notification.id == notificationId) {
                            updatedNotification
                        } else {
                            notification
                        }
                    }
                    _uiState.value = _uiState.value.copy(notifications = updatedList)
                    
                    // Actualizar el contador
                    loadNotificationCount()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al marcar notificación como leída: ${e.message}"
                )
            }
        }
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
