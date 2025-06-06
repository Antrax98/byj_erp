package dev.byjtech.erp.core.moduleRoot.nav.superHome

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.*
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companies.CompaniesComponent
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companies.CompaniesComponentImpl

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

    private val _state = MutableStateFlow(SuperHomeState())
    override val state: StateFlow<SuperHomeState> = _state.asStateFlow()

    //navegacion
    @Serializable
    sealed class Config {
        @Serializable
        data object Companies : Config()
    }

    private val navigation = StackNavigation<Config>()

    private val stack  = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Companies) },
        handleBackButton = true,
        childFactory = ::childFactory
    )

    override val childStack: Value<ChildStack<*, SuperHomeComponent.Child>> = stack

    private fun companiesComponent(componentContext: ComponentContext): CompaniesComponent =
        CompaniesComponentImpl(componentContext)

    private fun childFactory(config: Config, componentContext: ComponentContext): SuperHomeComponent.Child {
        return when (config) {
            is Config.Companies -> SuperHomeComponent.Child.Companies(companiesComponent(componentContext))
        }

    }

}