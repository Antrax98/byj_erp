package dev.byjtech.erp.common

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.api.ApiClient
import kotlinx.coroutines.flow.StateFlow

fun interface ModuleRootComponentFactory {
    fun create(context: ComponentContext, userPermissions: StateFlow<Set<PermissionKey>>, apiClient: ApiClient, toHome: () -> Unit): ModuleRootComponent
}