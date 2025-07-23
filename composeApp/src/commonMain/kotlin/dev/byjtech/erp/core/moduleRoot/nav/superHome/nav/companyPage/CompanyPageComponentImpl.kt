package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companyPage

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.CompanyDTO
import dev.byjtech.erp.core.dto.ModuleDTO
import dev.byjtech.erp.core.dto.SubscriptionDTO
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeComponentImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CompanyPageComponentImpl(
    componentContext: ComponentContext,
    override val company: CompanyDTO,
    override val apiClient: ApiClient,
    override val navTo: (SuperHomeComponentImpl.Config) -> Unit
): CompanyPageComponent, ComponentContext by componentContext {

    val coroutineScope = componentContext.coroutineScope()

    private val _subsModMap = MutableStateFlow<Map<SubscriptionDTO, ModuleDTO>>(emptyMap())
    override val subsModMap: StateFlow<Map<SubscriptionDTO, ModuleDTO>> = _subsModMap.asStateFlow()

    override fun navToAddSubscription(company: CompanyDTO) {
        val modulesSet: Set<ModuleDTO> = subsModMap.value.values.toSet()
        println("modulesSet: $modulesSet")
        navTo(SuperHomeComponentImpl.Config.AddSubscription(company, modulesSet))
    }

    override fun changeSubscriptionAccess(subscriptionId: String, isAllowed: Boolean) {
        coroutineScope.launch {
            val response = apiClient.subscriptionsSA.changeSubscriptionAccess(subscriptionId, isAllowed)
            when (response) {
                is dev.byjtech.erp.common.ApiResponse.Success -> {
                    loadSubsModMap()
                }
                else -> {
                    //todo:manejar errores si es que hay (de red?)
                }

            }
        }

    }

    private fun loadSubsModMap() {
        coroutineScope.launch {
            val response = apiClient.subscriptionsSA.getCompanySubscriptions(company.id)
            when (response) {
                is dev.byjtech.erp.common.ApiResponse.Success -> {
                    val subsMap = response.data.associate { it.subscription to it.module }
                    _subsModMap.value = subsMap
                }
                is dev.byjtech.erp.common.ApiResponse.Error -> {
                    when(response.code){
                        //todo:manejar errores
                    }
                }
            }

        }
    }

    init {
        loadSubsModMap()
    }

}