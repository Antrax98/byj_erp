package dev.byjtech.erp.core.moduleRoot

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.common.ModuleEntry
import dev.byjtech.erp.common.ModuleManager
import dev.byjtech.erp.common.old.OldModuleManager
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager
import dev.byjtech.erp.common.session.SessionNavigationTarget
import dev.byjtech.erp.core.moduleRoot.nav.auth.LoginComponent
import dev.byjtech.erp.core.moduleRoot.nav.auth.LoginComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.home.HomeComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.HomeComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.splash.SplashComponent
import dev.byjtech.erp.core.moduleRoot.nav.splash.SplashComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeComponent
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeComponentImpl
import kotlinx.serialization.Serializable
import org.koin.mp.KoinPlatform.getKoin

class RootComponentImpl(
    componentContext: ComponentContext,
    private val sessionManager: SessionManager,
    private val apiClient: ApiClient
): RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    private val stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Splash) },
        handleBackButton = true,
        childFactory = ::child
    )

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = stack

    @Serializable
    sealed class Config {
        @Serializable
        data object Home : Config()

        @Serializable
        data object Login : Config()

        @Serializable
        data object Splash : Config()

        @Serializable
        data object SuperHome : Config()
    }

    private val koin = getKoin()
    //private val oldModuleManager = koin.get<OldModuleManager>()
    private val moduleEntrySet = koin.getAll<ModuleEntry>().toSet()

    //TODO, en vez de hacer estas funciones, se les puede entrega su contenido directamente al fun child
    private fun homeComponent(componentContext: ComponentContext): HomeComponent =
        HomeComponentImpl(componentContext, sessionManager, apiClient, moduleManager = ModuleManager(moduleEntrySet))
    private fun loginComponent(componentContext: ComponentContext): LoginComponent =
        LoginComponentImpl(componentContext, sessionManager)
    private fun splashComponent(componentContext: ComponentContext): SplashComponent =
        SplashComponentImpl(componentContext, sessionManager,)
    private fun superHomeComponent(componentContext: ComponentContext): SuperHomeComponent =
        SuperHomeComponentImpl(componentContext, sessionManager, apiClient)

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Home -> RootComponent.Child.Home(homeComponent(componentContext.childContext("home")))
            is Config.Login -> RootComponent.Child.Login(loginComponent(componentContext.childContext("login")))
            is Config.Splash -> RootComponent.Child.Splash(splashComponent(componentContext.childContext("splash")))
            is Config.SuperHome -> RootComponent.Child.SuperHome(superHomeComponent(componentContext.childContext("superHome")))
        }



    private fun navigateToLogin(navigation: StackNavigation<Config>) {
        println("current child stack: ${childStack.value}")
        val currentScreen = childStack.value.active.instance
        if (currentScreen !is RootComponent.Child.Login) {
            println("Navigating to Login")
            try {
                navigation.replaceCurrent(Config.Login)
            } catch (e: Exception) {
                println("Error navigating to Login: ${e.message}")
            }
        }
    }

    private fun navigateToHome(navigation: StackNavigation<Config>) {
        println("current child stack: ${childStack.value}")
        val currentScreen = childStack.value.active.instance
        if (currentScreen !is RootComponent.Child.Home) {
            println("Navigating to Home")
            navigation.replaceCurrent(Config.Home)
        }
    }

    private fun navigateToSuperHome(navigation: StackNavigation<Config>) {
        println("current child stack: ${childStack.value}")
        val currentScreen = childStack.value.active.instance
        if (currentScreen !is RootComponent.Child.SuperHome) {
            println("Navigating to SuperHome")
            navigation.replaceCurrent(Config.SuperHome)
        }
    }

    private fun navigateToSplash(navigation: StackNavigation<Config>) {
        println("current child stack: ${childStack.value}")
        val currentScreen = childStack.value.active.instance
        if (currentScreen !is RootComponent.Child.Splash) {
            println("Navigating to Splash")
            navigation.replaceCurrent(Config.Splash)
        }
    }

    private fun navigateTo(target: SessionNavigationTarget) {
        val config = when (target) {
            SessionNavigationTarget.Login -> Config.Login
            SessionNavigationTarget.Home -> Config.Home
            SessionNavigationTarget.SuperHome -> Config.SuperHome
            SessionNavigationTarget.Splash -> Config.Splash
        }

        val current = childStack.value.active.configuration
        if (current != config) {
            navigation.replaceCurrent(config)
        }
    }

    init {
        sessionManager.onNavigationRequired = ::navigateTo
    }


}

