package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.common.session.SessionManager
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addPermission.AddPermissionComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addRole.AddRoleComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolePage.RolePageComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolesMain.RolesMainComponent

interface RolesFeatureComponent: FeatureComponent {

    val childStack: Value<ChildStack<*, Child>>
    val sessionManagerRef: SessionManager?

    sealed class Child {
        class RolesMain(val component: RolesMainComponent): Child()
        class RolePage(val component: RolePageComponent): Child()
        class AddRole(val component: AddRoleComponent): Child()
        class AddPermission(val component: AddPermissionComponent): Child()
    }
}