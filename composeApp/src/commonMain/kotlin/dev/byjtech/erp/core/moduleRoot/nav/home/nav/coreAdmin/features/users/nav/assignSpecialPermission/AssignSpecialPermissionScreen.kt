package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.assignSpecialPermission

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.byjtech.erp.common.UnderConstructionScreen

@Composable
fun AssignSpecialPermissionScreen(component: AssignSpecialPermissionComponent) {
    UnderConstructionScreen("AssignSpecialPermission")
    Button(onClick = { component.onFinished(true)}){
        Text("Finish test")

    }
}