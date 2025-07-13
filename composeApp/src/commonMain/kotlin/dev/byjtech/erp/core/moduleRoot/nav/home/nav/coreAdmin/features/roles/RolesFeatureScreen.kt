package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import dev.byjtech.erp.common.UnderConstructionScreen
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolesMain.RolesMainScreen
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.RolesFeatureComponent.Child
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addPermission.AddPermissionScreen
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addRole.AddRoleScreen
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolePage.RolePageScreen

@Composable
fun RolesFeatureScreen(component: RolesFeatureComponent) {

    Children(
        stack = component.childStack,
        animation = stackAnimation(slide())
    ){
        when(val child = it.instance){
            is Child.RolesMain -> RolesMainScreen(component = child.component)
            is Child.RolePage -> RolePageScreen(component = child.component)
            is Child.AddRole -> AddRoleScreen(component = child.component)
            is Child.AddPermission -> AddPermissionScreen(component = child.component)
        }
    }
}