package dev.byjtech.erp.core.moduleRoot.nav.auth

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.session.SessionManager

class LoginComponentImpl(
    componentContext: ComponentContext,
    private val sessionManager: SessionManager,
): LoginComponent, ComponentContext by componentContext {
    override suspend fun onLoginClicked() {
        sessionManager.initiateLogin()
    }
}