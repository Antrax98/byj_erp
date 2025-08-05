package dev.byjtech.erp.modules.machinery.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.modules.machinery.create.MachineryCreateComponent
import dev.byjtech.erp.modules.machinery.create.MachineryCreateComponentImpl
import dev.byjtech.erp.modules.machinery.list.MachineryListComponent
import dev.byjtech.erp.modules.machinery.list.MachineryListComponentImpl
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

class MachineryMainComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val toHome: () -> Unit,
    override val updateTitle: (newTitle: String) -> Unit
) : MachineryMainComponent, ComponentContext by componentContext {

    private val coroutineScope = componentContext.coroutineScope()

    @Serializable
    sealed class Config {
        @Serializable
        data object Main : Config()
        @Serializable
        data object List : Config()
        @Serializable
        data object Create : Config()
    }

    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, MachineryMainComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Main,
            handleBackButton = true,
            childFactory = ::childFactory
        )

    private fun childFactory(config: Config, componentContext: ComponentContext): MachineryMainComponent.Child {
        return when (config) {
            is Config.Main -> MachineryMainComponent.Child.Main(
                MachineryMainMenuComponentImpl(
                    componentContext = componentContext.childContext("main"),
                    userPermissions = userPermissions,
                    onNavigateToList = ::navigateToList,
                    onNavigateToCreate = ::navigateToCreate
                )
            )
            is Config.List -> MachineryMainComponent.Child.List(
                MachineryListComponentImpl(
                    componentContext = componentContext.childContext("list"),
                    userPermissions = userPermissions,
                    apiClient = apiClient,
                    toHome = ::navigateToMain,
                    updateTitle = updateTitle
                )
            )
            is Config.Create -> MachineryMainComponent.Child.Create(
                MachineryCreateComponentImpl(
                    componentContext = componentContext.childContext("create"),
                    userPermissions = userPermissions,
                    apiClient = apiClient,
                    toHome = ::navigateToMain,
                    updateTitle = updateTitle,
                    onMachineryCreated = ::navigateToList
                )
            )
        }
    }

    override fun navigateToList() {
        navigation.push(Config.List)
    }

    override fun navigateToCreate() {
        navigation.push(Config.Create)
    }

    override fun navigateToMain() {
        navigation.pop()
    }

    override fun onBack(): Boolean {
        if (childStack.active.configuration is Config.Main) {
            return false // Let parent handle
        }
        navigation.pop()
        return true
    }

    init {
        updateTitle("Maquinaria")
    }
}
