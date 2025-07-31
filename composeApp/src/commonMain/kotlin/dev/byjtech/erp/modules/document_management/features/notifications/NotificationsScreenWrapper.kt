package dev.byjtech.erp.modules.document_management.features.notifications

import androidx.compose.runtime.*
import dev.byjtech.erp.document_management.request.UpdateNotificationSettingsRequest

@Composable
fun NotificationsScreenWrapper(component: NotificationsComponent) {
    val state by component.viewModel.uiState.collectAsState()
    
    NotificationsScreen(
        state = state,
        onLoadNotifications = {
            component.viewModel.loadNotifications()
        },
        onMarkAsRead = { notificationId ->
            component.viewModel.markAsRead(notificationId)
        },
        onDismissError = {
            component.viewModel.dismissError()
        },
        onNavigateToSettings = {
            component.onNavigateToSettings()
        }
    )
}
