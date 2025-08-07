package dev.byjtech.erp.modules.machinery.main

import androidx.compose.runtime.*
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import dev.byjtech.erp.modules.machinery.create.MachineryCreateScreen
import dev.byjtech.erp.modules.machinery.edit.MachineryEditScreen
import dev.byjtech.erp.modules.machinery.history.MachineryHistoryScreen
import dev.byjtech.erp.modules.machinery.list.MachineryListScreen

@Composable
fun MachineryMainScreen(component: MachineryMainComponent) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(fade())
    ) { child ->
        when (val instance = child.instance) {
            is MachineryMainComponent.Child.Main -> MachineryMainMenuScreen(instance.component)
            is MachineryMainComponent.Child.List -> MachineryListScreen(instance.component)
            is MachineryMainComponent.Child.Create -> MachineryCreateScreen(instance.component)
            is MachineryMainComponent.Child.InactiveList -> MachineryListScreen(instance.component)
            is MachineryMainComponent.Child.Edit -> MachineryEditScreen(instance.component)
            is MachineryMainComponent.Child.History -> MachineryHistoryScreen(instance.component)
        }
    }
}
