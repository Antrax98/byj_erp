package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.usersMain

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.byjtech.erp.common.getPlatformName

@Composable
fun UsersMainScreen(component: UsersMainComponent) {
    Column {
        Text("Users Main Screen")
        Text("actual platform ${getPlatformName()}")
    }
}