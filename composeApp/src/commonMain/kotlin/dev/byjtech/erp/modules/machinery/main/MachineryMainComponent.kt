package dev.byjtech.erp.modules.machinery.main

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.modules.machinery.create.MachineryCreateComponent
import dev.byjtech.erp.modules.machinery.list.MachineryListComponent

interface MachineryMainComponent : FeatureComponent {
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Main(val component: MachineryMainMenuComponent) : Child()
        class List(val component: MachineryListComponent) : Child()
        class Create(val component: MachineryCreateComponent) : Child()
    }

    fun navigateToList()
    fun navigateToCreate()
    fun navigateToMain()
}
