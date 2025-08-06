package dev.byjtech.erp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import dev.byjtech.erp.core.moduleRoot.RootComponentImpl
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager
import dev.byjtech.erp.common.session.provideSettings
import io.ktor.client.engine.cio.CIO
import com.arkivanov.essenty.lifecycle.*
import dev.byjtech.erp.common.session.DesktopGoogleLoginHandler
import dev.byjtech.erp.di.initKoin
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers

fun main() {
    initKoin()
    application {

        val settings = provideSettings()
        val dotenv = dotenv {
            ignoreIfMissing = false
        }
        val apiClient = ApiClient(
            engine = CIO.create(),
            baseUrl = dotenv["BASE_URL"],
            basePort = dotenv["BASE_PORT"]?.toIntOrNull(),
            settings = settings,
            dispatcher = Dispatchers.IO,
        )

        val lifecycle = LifecycleRegistry()

        val sessionManager = SessionManager(apiClient, settings, null) {
        }

        val googleLoginHandler = DesktopGoogleLoginHandler(settings) { session ->
            sessionManager.setAppSession(session)
        }

        sessionManager.loginHandler = googleLoginHandler

        val root = RootComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            sessionManager = sessionManager,
            apiClient = apiClient
        )

        Window(onCloseRequest = {
            googleLoginHandler.stopServer()
            lifecycle.destroy()
            exitApplication()
        }, title = "ByJ ERP Desktop") {
            App(root)
        }
    }
}