package dev.byjtech.erp.common

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager
import kotlinx.coroutines.flow.StateFlow

fun interface FeatureComponentFactory {
    fun create(context: ComponentContext, userPermissions: StateFlow<Set<PermissionKey>>, apiClient: ApiClient, toHome: () -> Unit, updateTitle: (newTitle: String) -> Unit): FeatureComponent
}