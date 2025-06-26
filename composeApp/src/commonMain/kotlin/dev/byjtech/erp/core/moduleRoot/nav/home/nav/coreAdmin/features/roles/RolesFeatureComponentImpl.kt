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
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addRole.AddRoleComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addRole.AddRoleComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolePage.RolePageComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolePage.RolePageComponentImpl

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
        @Serializable
        data class RolePage(val roleId: String) : Config()
        @Serializable
        data object AddRole : Config()
    }

    private val navigation = StackNavigation<Config>()

    private fun rolesMain(componentContext: ComponentContext): RolesMainComponent =
        RolesMainComponentImpl(componentContext, userPermissions, apiClient, ::navigateTo)

    private fun rolePage(componentContext: ComponentContext, roleId: String): RolePageComponent =
        RolePageComponentImpl(componentContext, userPermissions,roleId, apiClient, ::navigateTo)

    private fun addRole(componentContext: ComponentContext): AddRoleComponent =
        AddRoleComponentImpl(componentContext, userPermissions, apiClient, ::navigateTo)

    private fun childFactory(config: Config, componentContext: ComponentContext): Child {
        return when (config) {
            is Config.RolesMain -> Child.RolesMain(rolesMain(componentContext.childContext("rolesMain")))
            is Config.RolePage -> Child.RolePage(rolePage(componentContext.childContext("rolePage"), config.roleId))
            is Config.AddRole -> Child.AddRole(addRole(componentContext.childContext("addRole")))
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
        if(childStack.active.configuration==Config.RolesMain){
            return false
        } else{
            navigation.pop()
            return true
        }
    }

    private fun navigateTo(target: Config) {
        navigation.pushNew(target)
    }
}