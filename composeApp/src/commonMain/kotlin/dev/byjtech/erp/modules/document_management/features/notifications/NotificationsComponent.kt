package dev.byjtech.erp.modules.document_management.features.notifications

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.modules.document_management.api.DocumentManagementClient
import kotlinx.coroutines.flow.StateFlow

interface NotificationsComponent {
    val componentContext: ComponentContext
    val viewModel: NotificationsViewModel
    
    fun onNavigateBack()
    fun onNavigateToSettings()
}

class NotificationsComponentImpl(
    override val componentContext: ComponentContext,
    override val viewModel: NotificationsViewModel,
    private val onNavigateBackCallback: () -> Unit,
    private val onNavigateToSettingsCallback: () -> Unit
) : NotificationsComponent {
    
    override fun onNavigateBack() {
        onNavigateBackCallback()
    }
    
    override fun onNavigateToSettings() {
        onNavigateToSettingsCallback()
    }
}
