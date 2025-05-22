package dev.byjtech.erp.core.moduleRoot.nav.superHome

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager

class SuperHomeComponentImpl (
    componentContext: ComponentContext,
    private val sessionManager: SessionManager,
    private val api: ApiClient
): SuperHomeComponent, ComponentContext by componentContext {
    override suspend fun onLogout() {
        sessionManager.logout()
    }

    override suspend fun onTestClick() {
        api.coreAuth.test()
    }
}