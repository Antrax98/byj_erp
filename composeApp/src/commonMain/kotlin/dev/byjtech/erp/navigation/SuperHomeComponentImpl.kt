package dev.byjtech.erp.navigation

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.api.ApiClient
import dev.byjtech.erp.session.SessionManager

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