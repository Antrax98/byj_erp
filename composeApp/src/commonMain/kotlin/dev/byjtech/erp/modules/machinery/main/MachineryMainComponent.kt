package dev.byjtech.erp.modules.machinery.main

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.modules.machinery.create.MachineryCreateComponent
import dev.byjtech.erp.modules.machinery.dto.MachineryDTO
import dev.byjtech.erp.modules.machinery.edit.MachineryEditComponent
import dev.byjtech.erp.modules.machinery.history.MachineryHistoryComponent
import dev.byjtech.erp.modules.machinery.list.MachineryListComponent

interface MachineryMainComponent : FeatureComponent {
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Main(val component: MachineryMainMenuComponent) : Child()
        class List(val component: MachineryListComponent) : Child()
        class InactiveList(val component: MachineryListComponent) : Child()
        class Create(val component: MachineryCreateComponent) : Child()
        class Edit(val component: MachineryEditComponent) : Child()
        class History(val component: MachineryHistoryComponent) : Child()
    }

    fun navigateToList()
    fun navigateToInactiveList()
    fun navigateToCreate()
    fun navigateToEdit(machinery: MachineryDTO)
    fun navigateToHistory(machinery: MachineryDTO)
    fun navigateToMain()
}
