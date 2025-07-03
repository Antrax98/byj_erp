package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager
import dev.byjtech.erp.core.dto.ModuleDTO
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolesMain.RolesMainComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolesMain.RolesMainComponentImpl
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.RolesFeatureComponent.Child
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addPermission.AddPermissionComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addPermission.AddPermissionComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addRole.AddRoleComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addRole.AddRoleComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolePage.RolePageComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolePage.RolePageComponentImpl
import kotlinx.coroutines.launch

class RolesFeatureComponentImpl(
    componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val sessionManagerRef: SessionManager?,
    override val toHome: () -> Unit,
    override val updateTitle: (newTitle: String) -> Unit
): RolesFeatureComponent, ComponentContext by componentContext {

    val coroutineScope = componentContext.coroutineScope()

    //navegacion
    @Serializable
    sealed class Config {
        @Serializable
        data object RolesMain : Config()
        @Serializable
        data class RolePage(val roleId: String) : Config()
        @Serializable
        data object AddRole : Config()
        @Serializable
        data class AddPermission(val roleId: String) : Config()
    }

    private val navigation = StackNavigation<Config>()

    private fun rolesMain(componentContext: ComponentContext): RolesMainComponent =
        RolesMainComponentImpl(componentContext, userPermissions, apiClient, ::navigateTo)

    private fun rolePage(componentContext: ComponentContext, roleId: String): RolePageComponent =
        RolePageComponentImpl(componentContext, userPermissions,roleId, apiClient, ::navigateTo)

    private fun addRole(componentContext: ComponentContext): AddRoleComponent =
        AddRoleComponentImpl(componentContext, userPermissions, apiClient, ::navigateTo){
            added ->
            navigation.pop {
                if (added && childStack.active.configuration == Config.RolesMain) {
                    val rolesMain = (childStack.active.instance as? Child.RolesMain)?.component
                    rolesMain?.let {
                        coroutineScope.launch {
                            it.fetchAllRoles()
                        }
                    }
                }
            }
        }

    private fun addPermission(componentContext: ComponentContext, roleId: String): AddPermissionComponent =
        AddPermissionComponentImpl(componentContext, userPermissions =  userPermissions, roleId = roleId, apiClient = apiClient , actModules = sessionManagerRef!!.allowedModules.value, onFinished = {
                added ->
            navigation.pop {
                println(childStack.active.configuration)
                if (added && childStack.active.configuration is Config.RolePage) {
                    val rolePage = (childStack.active.instance as? Child.RolePage)?.component
                    println("a punto de cargar permisos (funcion desde el rolesFeature)")
                    rolePage?.fetchRolePermissions()
                }
            }
        })

    private fun childFactory(config: Config, componentContext: ComponentContext): Child {
        return when (config) {
            is Config.RolesMain -> Child.RolesMain(rolesMain(componentContext.childContext("rolesMain")))
            is Config.RolePage -> Child.RolePage(rolePage(componentContext.childContext("rolePage"), config.roleId))
            is Config.AddRole -> Child.AddRole(addRole(componentContext.childContext("addRole")))
            is Config.AddPermission -> Child.AddPermission(addPermission(componentContext.childContext("addPermission"), config.roleId))
        }
    }

    private val stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.RolesMain) },
        handleBackButton = true,
        childFactory = ::childFactory
    )

    override val childStack: Value<ChildStack<Config, Child>> = stack

    override fun onBack(): Boolean {
        if(childStack.active.configuration==Config.RolesMain){
            return false
        } else{
            navigation.pop(){
                val newConfig = childStack.active.configuration
                changeTitle(newConfig)
            }
            return true
        }
    }

    private fun navigateTo(target: Config) {
        navigation.pushNew(target)
        changeTitle(target)
    }

    private fun changeTitle(config: Config){
        when(config){
            is Config.RolesMain -> updateTitle("Roles")
            is Config.RolePage -> updateTitle("Rol")
            is Config.AddRole -> updateTitle("Añadir Rol")
            is Config.AddPermission -> updateTitle("Añadir Permiso")
        }
    }

    //necesario para darle el titulo al comienzo
    init {
        updateTitle("Roles")
    }
}