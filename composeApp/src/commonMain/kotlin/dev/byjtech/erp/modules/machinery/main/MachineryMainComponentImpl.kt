package dev.byjtech.erp.modules.machinery.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.modules.machinery.api.MachineryApiImpl
import dev.byjtech.erp.modules.machinery.create.MachineryCreateComponent
import dev.byjtech.erp.modules.machinery.create.MachineryCreateComponentImpl
import dev.byjtech.erp.modules.machinery.dto.MachineryDTO
import dev.byjtech.erp.modules.machinery.edit.MachineryEditComponent
import dev.byjtech.erp.modules.machinery.edit.MachineryEditComponentImpl
import dev.byjtech.erp.modules.machinery.history.MachineryHistoryComponent
import dev.byjtech.erp.modules.machinery.history.MachineryHistoryComponentImpl
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
        @Serializable
        data object InactiveList : Config()
        @Serializable
        data class Edit(val machinery: MachineryDTO) : Config()
        @Serializable
        data class History(val machinery: MachineryDTO) : Config()
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
                    onNavigateToInactiveList = ::navigateToInactiveList,
                    onNavigateToCreate = ::navigateToCreate
                )
            )
            is Config.List -> MachineryMainComponent.Child.List(
                MachineryListComponentImpl(
                    componentContext = componentContext.childContext("list"),
                    userPermissions = userPermissions,
                    apiClient = apiClient,
                    toHome = ::navigateToMain,
                    updateTitle = updateTitle,
                    onNavigateToEdit = ::navigateToEdit,
                    onNavigateToHistory = ::navigateToHistory
                ).apply {
                    updateTitle("Maquinarias Activas")
                }
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
            is Config.InactiveList -> MachineryMainComponent.Child.InactiveList(
                MachineryListComponentImpl(
                    componentContext = componentContext.childContext("inactive-list"),
                    userPermissions = userPermissions,
                    apiClient = apiClient,
                    toHome = ::navigateToMain,
                    updateTitle = updateTitle,
                    onNavigateToEdit = ::navigateToEdit,
                    onNavigateToHistory = ::navigateToHistory,
                    loadInactive = true
                )
            )
            is Config.Edit -> MachineryMainComponent.Child.Edit(
                MachineryEditComponentImpl(
                    componentContext = componentContext.childContext("edit"),
                    machineryApi = MachineryApiImpl(apiClient),
                    apiClient = apiClient,
                    machinery = config.machinery,
                    toHome = ::navigateToMain,
                    updateTitle = updateTitle,
                    onNavigateBack = ::navigateToMain
                )
            )
            is Config.History -> MachineryMainComponent.Child.History(
                MachineryHistoryComponentImpl(
                    componentContext = componentContext.childContext("history"),
                    userPermissions = userPermissions,
                    apiClient = apiClient,
                    toHome = ::navigateToMain,
                    updateTitle = updateTitle,
                    machineryId = config.machinery.id,
                    machineryName = config.machinery.name
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

    override fun navigateToInactiveList() {
        navigation.push(Config.InactiveList)
    }

    override fun navigateToEdit(machinery: MachineryDTO) {
        navigation.push(Config.Edit(machinery))
    }

    override fun navigateToHistory(machinery: MachineryDTO) {
        navigation.push(Config.History(machinery))
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
