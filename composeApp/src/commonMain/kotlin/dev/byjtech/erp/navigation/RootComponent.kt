package dev.byjtech.erp.navigation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.core.home.HomeComponent

interface RootComponent {
    val childStack: Value<ChildStack<*, Child>>


    sealed class Child {
        class Home(val component: HomeComponent): Child()
        class Login(val component: LoginComponent): Child()
        class Splash(val component: SplashComponent): Child()
        class SuperHome(val component: SuperHomeComponent): Child()
    }

}