package dev.byjtech.erp.modules.document_management.features.notifications

import com.arkivanov.decompose.ComponentContext

interface NotificationSettingsComponent {
    val componentContext: ComponentContext
    val viewModel: NotificationSettingsViewModel
    
    fun onNavigateBack()
}

class NotificationSettingsComponentImpl(
    override val componentContext: ComponentContext,
    override val viewModel: NotificationSettingsViewModel,
    private val onNavigateBackCallback: () -> Unit
) : NotificationSettingsComponent {
    
    override fun onNavigateBack() {
        onNavigateBackCallback()
    }
}
