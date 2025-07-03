package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.addUser.AddUserComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.addUser.AddUserComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.assignRole.AssignRoleComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.assignRole.AssignRoleComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.assignSpecialPermission.AssignSpecialPermissionComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.assignSpecialPermission.AssignSpecialPermissionComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.usersMain.UsersMainComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.usersMain.UsersMainComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.userPage.UserPageComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.userPage.UserPageComponentImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class UsersFeatureComponentImpl(
    val componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val toHome: () -> Unit,
    override val updateTitle: (newTitle: String) -> Unit
) : UsersFeatureComponent, ComponentContext by componentContext {


    private val coroutineScope = componentContext.coroutineScope()

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
        data class UserPage(val userId: String) : Config()
        @Serializable
        data class AssignRole(val userId: String, val assignableRoles: Set<RoleDTO>) : Config()
        @Serializable
        data class AssignSpecialPermission(val userId: String, val assignablePermissions: Set<PermissionKey>) : Config()
        @Serializable
        data object AddUser : Config()
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

    private fun userPageComponent(componentContext: ComponentContext, userId: String): UserPageComponent =
        UserPageComponentImpl(componentContext, userPermissions, userId, apiClient, ::navigateTo)

    private fun assignRoleComponent(componentContext: ComponentContext, userId: String, assignableRoles: Set<RoleDTO>): AssignRoleComponent =
        AssignRoleComponentImpl(
            componentContext,
            userPermissions,
            userId,
            assignableRoles
        ){
            assigned ->
            navigation.pop {
                if (assigned && childStack.active.configuration == Config.UserPage) {
                    val userPage = (childStack.active.instance as? UsersFeatureComponent.Child.UserPage)?.component
                    userPage?.let {
                        coroutineScope.launch {
                            it.fetchUserRoles()
                        }
                    }
                }
            }
        }

    private fun assignSpecialPermissionComponent(componentContext: ComponentContext, userId: String, assignablePermissions: Set<PermissionKey>): AssignSpecialPermissionComponent =
        AssignSpecialPermissionComponentImpl(
            componentContext,
            userPermissions,
            userId,
            assignablePermissions
        ) { assigned ->
            navigation.pop {
                if (assigned && childStack.active.configuration == Config.UserPage) {
                    val userPage =
                        (childStack.active.instance as? UsersFeatureComponent.Child.UserPage)?.component
                    userPage?.let {
                        coroutineScope.launch {
                            it.fetchUserSpecialPermissions()
                        }
                    }
                }
            }
        }

    private fun addUserComponent(componentContext: ComponentContext): AddUserComponent =
        AddUserComponentImpl(
            componentContext,
            apiClient,
            userPermissions
        ){
            added ->
            navigation.pop {
                if (added && childStack.active.configuration == Config.UsersMain) {
                    val usersMain = (childStack.active.instance as? UsersFeatureComponent.Child.UsersMain)?.component
                    usersMain?.let {
                        coroutineScope.launch {
                            it.loadUsers()
                        }
                    }
                }
            }
        }

    private fun childFactory(config: Config, componentContext: ComponentContext): UsersFeatureComponent.Child {
        return when (config) {
            is Config.UsersMain -> UsersFeatureComponent.Child.UsersMain(usersMainComponent(componentContext.childContext("usersMain")))
            is Config.UserPage -> UsersFeatureComponent.Child.UserPage(
                userPageComponent(
                    componentContext.childContext("userPage"),
                    userId = config.userId
                )
            )
            is Config.AssignRole -> UsersFeatureComponent.Child.AssignRole(
                assignRoleComponent(
                    componentContext.childContext("assignRole"),
                    userId = config.userId,
                    assignableRoles = config.assignableRoles
                )
            )
            is Config.AssignSpecialPermission -> UsersFeatureComponent.Child.AssignSpecialPermission(
                assignSpecialPermissionComponent(
                    componentContext.childContext("assignSpecialPermission"),
                    userId = config.userId,
                    assignablePermissions = config.assignablePermissions
                )
            )
            is Config.AddUser -> UsersFeatureComponent.Child.AddUser(
                addUserComponent(componentContext.childContext("addUser"))
            )

        }

    }

    private fun navigateTo(target: Config) {
        navigation.pushNew(target)
    }

}