package dev.byjtech.erp.common.old

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import kotlinx.coroutines.flow.StateFlow

fun interface OldModuleRootComponentFactory {
    fun create(context: ComponentContext, userPermissions: StateFlow<Set<PermissionKey>>, apiClient: ApiClient, toHome: () -> Unit): OldModuleRootComponent
}