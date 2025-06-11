package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companies

import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.CompanyDTO
import kotlinx.coroutines.flow.StateFlow
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeComponentImpl.Config

interface CompaniesComponent {
    val state: StateFlow<CompaniesState>
    val apiClient: ApiClient
    val isLoading: StateFlow<Boolean>
    val companiesList: StateFlow<List<CompanyDTO>?>
    fun loadCompanies()
    val navTo: (Config) -> Unit

}