package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolesMain.RolesMainComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolesMain.RolesMainComponentImpl
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.RolesFeatureComponent.Child

class RolesFeatureComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val toHome: () -> Unit
): RolesFeatureComponent, ComponentContext by componentContext {




    //navegacion
    @Serializable
    sealed class Config {
        @Serializable
        data object RolesMain : Config()
    }

    private val navigation = StackNavigation<Config>()

    private fun rolesMain(componentContext: ComponentContext): RolesMainComponent =
        RolesMainComponentImpl(componentContext, userPermissions, apiClient, ::navigateTo)

    private fun childFactory(config: Config, componentContext: ComponentContext): Child {
        return when (config) {
            is Config.RolesMain -> Child.RolesMain(rolesMain(componentContext.childContext("rolesMain")))
        }
    }

    private val stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.RolesMain) },
        handleBackButton = true,
        childFactory = ::childFactory
    )

    override val childStack: Value<ChildStack<*, Child>> = stack

    override fun onBack(): Boolean {
        return false
    }

    private fun navigateTo(target: Config) {
        navigation.pushNew(target)
    }
}