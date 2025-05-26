package dev.byjtech.erp.common

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager

fun interface ModuleRootComponentFactory {
    fun create(context: ComponentContext, sessionManager: SessionManager, apiClient: ApiClient, toHome: () -> Unit): ModuleRootComponent
}