package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import dev.byjtech.erp.common.UnderConstructionScreen
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolesMain.RolesMainScreen
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.RolesFeatureComponent.Child

@Composable
fun RolesFeatureScreen(component: RolesFeatureComponent) {

    Children(
        stack = component.childStack,
        animation = null
    ){
        when(val child = it.instance){
            is Child.RolesMain -> RolesMainScreen(component = child.component)
        }

    }
}