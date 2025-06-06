package dev.byjtech.erp.core.moduleRoot.nav.superHome

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companies.CompaniesComponent
import kotlinx.coroutines.flow.StateFlow

interface SuperHomeComponent {
    val state: StateFlow<SuperHomeState>
    val childStack: Value<ChildStack<*, Child>>
    suspend fun onLogout()
    suspend fun onTestClick()

    sealed class Child {
        class Companies(val component: CompaniesComponent) : Child()
    }
}