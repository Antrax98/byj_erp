package dev.byjtech.erp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import dev.byjtech.erp.core.moduleRoot.RootComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.HomeScreen
import dev.byjtech.erp.core.moduleRoot.nav.auth.LoginScreen
import dev.byjtech.erp.core.moduleRoot.nav.splash.SplashScreen
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeScreen
import dev.byjtech.erp.ui.theme.AppTheme

@Composable
fun App(root: RootComponent) {
    AppTheme(
        darkTheme = false,
    ) {
        Children(
            stack = root.childStack,
            animation = null //stackAnimation(slide())
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.Home -> HomeScreen(child.component)
                is RootComponent.Child.Login -> LoginScreen(child.component)
                is RootComponent.Child.Splash -> SplashScreen()
                is RootComponent.Child.SuperHome -> SuperHomeScreen(child.component)
            }
        }
    }
}