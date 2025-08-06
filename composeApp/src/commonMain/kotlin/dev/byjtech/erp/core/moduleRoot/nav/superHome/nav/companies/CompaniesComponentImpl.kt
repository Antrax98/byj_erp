package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companies

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.core.dto.CompanyDTO
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeComponentImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class CompaniesComponentImpl(
    componentContext: ComponentContext,
    override val apiClient: ApiClient,
    override val navTo: (SuperHomeComponentImpl.Config) -> Unit

): CompaniesComponent, ComponentContext by componentContext {

    private val coroutineScope = componentContext.coroutineScope()

    private val _state = MutableStateFlow(CompaniesState())
    override val state: StateFlow<CompaniesState> = _state.asStateFlow()

    private val _companiesList = MutableStateFlow<List<CompanyDTO>?>(null)
    override val companiesList: StateFlow<List<CompanyDTO>?> = _companiesList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    override fun loadCompanies() {
        coroutineScope.launch {
            _isLoading.value = true
            val response = apiClient.companiesSA.getAllCompanies()
            if(response.isNotEmpty()){
                _companiesList.value = response.toList()
            }else{
                _companiesList.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    init {
        loadCompanies()
    }

}

