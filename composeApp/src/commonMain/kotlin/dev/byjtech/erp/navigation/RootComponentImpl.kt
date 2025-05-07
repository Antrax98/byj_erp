package dev.byjtech.erp.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.api.ApiClient
import dev.byjtech.erp.session.SessionManager
import dev.byjtech.erp.session.SessionNavigationTarget
import kotlinx.serialization.Serializable

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

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Home -> RootComponent.Child.Home(homeComponent(componentContext))
            is Config.Login -> RootComponent.Child.Login(loginComponent(componentContext))
            is Config.Splash -> RootComponent.Child.Splash(splashComponent(componentContext))
            is Config.SuperHome -> RootComponent.Child.SuperHome(superHomeComponent(componentContext))
        }

    private fun homeComponent(componentContext: ComponentContext): HomeComponent =
        HomeComponentImpl(componentContext, sessionManager, apiClient)
    private fun loginComponent(componentContext: ComponentContext): LoginComponent =
        LoginComponentImpl(componentContext, sessionManager)
    private fun splashComponent(componentContext: ComponentContext): SplashComponent =
        SplashComponentImpl(componentContext, sessionManager,)
    private fun superHomeComponent(componentContext: ComponentContext): SuperHomeComponent =
        SuperHomeComponentImpl(componentContext, sessionManager, apiClient)

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



    init {
        sessionManager.onNavigationRequired = { target ->
            when(target){
                SessionNavigationTarget.Login -> {navigateToLogin(navigation)}
                SessionNavigationTarget.Home -> {navigateToHome(navigation)}
                SessionNavigationTarget.SuperHome -> {navigateToSuperHome(navigation)}
                SessionNavigationTarget.Splash -> {navigateToSplash(navigation)}
            }
        }
        //apiClient no necesita todas las rutas, pero si la de login
        apiClient.onNavigationRequired = { target ->
            when(target){
                SessionNavigationTarget.Login -> {navigateToLogin(navigation)}
                SessionNavigationTarget.Home -> {navigateToHome(navigation)}
                SessionNavigationTarget.SuperHome -> {navigateToSuperHome(navigation)}
                SessionNavigationTarget.Splash -> {navigateToSplash(navigation)}
            }
        }
    }
}

