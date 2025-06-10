package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.common.PermissionAwareComponent
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.moduleRoot.nav.home.HomeComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.usersMain.UsersMainComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.usersMain.UsersMainComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.userPage.UserPageComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.userPage.UserPageComponentImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

class UsersFeatureComponentImpl(
    val componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val toHome: () -> Unit
) : UsersFeatureComponent, ComponentContext by componentContext {


    private val _state = MutableStateFlow(UsersFeatureState())
    override val state: StateFlow<UsersFeatureState> = _state

    override fun onBack(): Boolean {
        if(childStack.active.configuration==Config.UsersMain){
            return false
        } else{
            navigation.pop()
            return true
        }
    }

    //navegacion
    @Serializable
    sealed class Config {
        @Serializable
        data object UsersMain : Config()
        @Serializable
        data class UserPage(val userId: Int) : Config()
    }

    private val navigation = StackNavigation<Config>()


    private val stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.UsersMain) },
        handleBackButton = true,
        childFactory = ::childFactory
    )

    override val childStack: Value<ChildStack<*, UsersFeatureComponent.Child>> = stack

    private fun usersMainComponent(componentContext: ComponentContext): UsersMainComponent =
        UsersMainComponentImpl(componentContext, userPermissions, apiClient, ::navigateTo)

    private fun userPageComponent(componentContext: ComponentContext, userId: Int): UserPageComponent =
        UserPageComponentImpl(componentContext, userPermissions, userId, apiClient)

    private fun childFactory(config: Config, componentContext: ComponentContext): UsersFeatureComponent.Child {
        return when (config) {
            is Config.UsersMain -> UsersFeatureComponent.Child.UsersMain(usersMainComponent(componentContext.childContext("usersMain")))
            is Config.UserPage -> UsersFeatureComponent.Child.UserPage(
                userPageComponent(
                    componentContext.childContext("userPage"),
                    userId = config.userId
                )
            )
        }

    }

    private fun navigateTo(target: Config) {
        navigation.pushNew(target)
    }

}