package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.superHomeMain

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeComponentImpl.Config

interface SuperHomeMainComponent {
    val componentContext: ComponentContext
    val apiClient: ApiClient
    val navTo: (Config) -> Unit
}