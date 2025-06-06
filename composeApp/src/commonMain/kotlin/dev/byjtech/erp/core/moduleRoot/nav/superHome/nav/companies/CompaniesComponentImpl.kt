package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companies

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.*

class CompaniesComponentImpl(
    componentContext: ComponentContext
): CompaniesComponent, ComponentContext by componentContext {

    private val _state = MutableStateFlow(CompaniesState())
    override val state: StateFlow<CompaniesState> = _state.asStateFlow()



}