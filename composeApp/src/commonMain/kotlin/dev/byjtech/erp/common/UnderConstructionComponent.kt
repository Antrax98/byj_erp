package dev.byjtech.erp.common

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.api.ApiClient
import kotlinx.coroutines.flow.StateFlow

class UnderConstructionComponent(
    val componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val toHome: () -> Unit,
    override val updateTitle: (newTitle: String) -> Unit,
    val featureName: String
) : FeatureComponent {

    override fun onBack(): Boolean {
        return false // No tiene navegación interna
    }

    init {
        updateTitle(featureName)
    }
}
