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
                _companiesList.value = dummyCompanies
            }
            _isLoading.value = false
        }
    }


    //datos de prueba
    private val dummyCompanies = listOf(
        CompanyDTO(
            id = 1,
            name = "TechNova Inc.",
            contactEmail = "contact@technova.com",
            createdAt = LocalDateTime.parse("2023-01-10T09:30:00"),
            updatedAt = LocalDateTime.parse("2024-05-01T15:45:00")
        ),
        CompanyDTO(
            id = 2,
            name = "GreenFields Ltd.",
            contactEmail = "info@greenfields.co",
            createdAt = LocalDateTime.parse("2022-11-05T13:15:00"),
            updatedAt = LocalDateTime.parse("2024-01-20T10:00:00")
        ),
        CompanyDTO(
            id = 3,
            name = "Skyreach Solutions",
            contactEmail = "support@skyreach.io",
            createdAt = LocalDateTime.parse("2021-06-18T08:00:00"),
            updatedAt = LocalDateTime.parse("2023-12-10T16:30:00")
        ),
        CompanyDTO(
            id = 4,
            name = "AquaCore Enterprises",
            contactEmail = "hello@aquacore.org",
            createdAt = LocalDateTime.parse("2023-03-22T11:20:00"),
            updatedAt = null
        ),
        CompanyDTO(
            id = 5,
            name = "NexusWorks",
            contactEmail = "team@nexus.works",
            createdAt = LocalDateTime.parse("2024-02-14T17:50:00"),
            updatedAt = LocalDateTime.parse("2025-01-10T09:00:00")
        )
    )

}

