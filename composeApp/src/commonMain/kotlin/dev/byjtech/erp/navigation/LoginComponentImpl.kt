package dev.byjtech.erp.navigation

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