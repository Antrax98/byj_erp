package dev.byjtech.erp.modules.document_management.features.notifications

import androidx.compose.runtime.*
import dev.byjtech.erp.document_management.request.UpdateNotificationSettingsRequest

@Composable
fun NotificationSettingsScreenWrapper(component: NotificationSettingsComponent) {
    val state by component.viewModel.uiState.collectAsState()
    
    NotificationSettingsScreen(
        state = state,
        onLoadSettings = {
            component.viewModel.loadSettings()
        },
        onUpdateSettings = { request ->
            component.viewModel.updateSettings(request)
        },
        onDaysChanged = { days ->
            component.viewModel.onDaysChanged(days)
        },
        onEmailEnabledChanged = { enabled ->
            component.viewModel.onEmailEnabledChanged(enabled)
        },
        onSystemEnabledChanged = { enabled ->
            component.viewModel.onSystemEnabledChanged(enabled)
        },
        onDismissError = {
            component.viewModel.dismissError()
        },
        onDismissSuccess = {
            component.viewModel.dismissSuccess()
        }
    )
}
