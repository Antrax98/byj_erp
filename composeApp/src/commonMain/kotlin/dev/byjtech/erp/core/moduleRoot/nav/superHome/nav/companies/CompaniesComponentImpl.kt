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
            id = "8f3b1824-0f8d-42c2-b394-278dfcc17b3a",
            name = "TechNova Inc.",
            contactEmail = "contact@technova.com",
            rut = "76.543.210-K",
            createdAt = LocalDateTime.parse("2023-01-10T09:30:00"),
            updatedAt = LocalDateTime.parse("2024-05-01T15:45:00"),
            billingId = null
        ),
        CompanyDTO(
            id = "64bcfe3c-ef92-4b82-931b-221bc163a6e4",
            name = "GreenFields Ltd.",
            contactEmail = "info@greenfields.co",
            rut = "80.112.345-2",
            createdAt = LocalDateTime.parse("2022-11-05T13:15:00"),
            updatedAt = LocalDateTime.parse("2024-01-20T10:00:00"),
            billingId = null
        ),
        CompanyDTO(
            id = "b4d49816-4de1-42c2-9d4f-e13ec5aebc3f",
            name = "Skyreach Solutions",
            contactEmail = "support@skyreach.io",
            rut = "89.654.321-7",
            createdAt = LocalDateTime.parse("2021-06-18T08:00:00"),
            updatedAt = LocalDateTime.parse("2023-12-10T16:30:00"),
            billingId = null
        ),
        CompanyDTO(
            id = "7d0caa1e-cc5b-4b1d-aeaf-4e00b84a4f76",
            name = "AquaCore Enterprises",
            contactEmail = "hello@aquacore.org",
            rut = "77.001.999-K",
            createdAt = LocalDateTime.parse("2023-03-22T11:20:00"),
            updatedAt = null,
            billingId = null
        ),
        CompanyDTO(
            id = "70f1ce91-8a63-4803-bfef-19a3681b86e0",
            name = "NexusWorks",
            contactEmail = "team@nexus.works",
            rut = "72.345.678-1",
            createdAt = LocalDateTime.parse("2024-02-14T17:50:00"),
            updatedAt = LocalDateTime.parse("2025-01-10T09:00:00"),
            billingId = null
        )
    )



}

