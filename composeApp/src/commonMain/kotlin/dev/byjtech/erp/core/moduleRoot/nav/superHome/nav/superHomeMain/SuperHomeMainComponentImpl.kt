package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.superHomeMain

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeComponentImpl.Config

class SuperHomeMainComponentImpl(
    override val componentContext: ComponentContext,
    override val apiClient: ApiClient,
    override val navTo: (Config) -> Unit
): SuperHomeMainComponent, ComponentContext by componentContext {

}