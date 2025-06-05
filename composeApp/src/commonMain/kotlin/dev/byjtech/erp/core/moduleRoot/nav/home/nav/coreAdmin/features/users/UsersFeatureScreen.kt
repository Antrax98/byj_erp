package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.usersMain.UsersMainScreen
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.usersMain.nav.userPage.UserPageScreen

@Composable
fun UsersFeatureScreen(component: UsersFeatureComponent) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(slide())
    ) {
        when (val child = it.instance) {
            is UsersFeatureComponent.Child.UsersMain -> UsersMainScreen(child.component)
            is UsersFeatureComponent.Child.UserPage -> UserPageScreen(child.component)
        }
    }
}