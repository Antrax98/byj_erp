package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.usersMain.UsersMainScreen

@Composable
fun UsersFeatureScreen(component: UsersFeatureComponent) {
    Children(
        stack = component.childStack,
        animation = null //stackAnimation(slide())
    ) {
        when (val child = it.instance) {
            is UsersFeatureComponent.Child.UsersMain -> UsersMainScreen(child.component)

        }

    }
}