package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.common.session.SessionManager
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.addUser.AddUserComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.assignRole.AssignRoleComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.assignSpecialPermission.AssignSpecialPermissionComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.usersMain.UsersMainComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.userPage.UserPageComponent
import kotlinx.coroutines.flow.StateFlow

interface UsersFeatureComponent: FeatureComponent {
    val state: StateFlow<UsersFeatureState>
    val sessionManagerRef: SessionManager?
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        class UsersMain(val component: UsersMainComponent): Child()
        class UserPage(val component: UserPageComponent) : Child()
        class AssignRole(val component: AssignRoleComponent): Child()
        class AssignSpecialPermission(val component: AssignSpecialPermissionComponent): Child()
        class AddUser(val component: AddUserComponent): Child()
    }
}