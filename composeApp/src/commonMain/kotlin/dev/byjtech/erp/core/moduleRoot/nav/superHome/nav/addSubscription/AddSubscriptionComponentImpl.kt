package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addSubscription

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.CompanyDTO
import dev.byjtech.erp.core.dto.ModuleDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddSubscriptionComponentImpl (
    componentContext: ComponentContext,
    override val apiClient: ApiClient,
    override val company: CompanyDTO,
    override val subModules: Set<ModuleDTO>,
    override val onFinished: (added: Boolean) -> Unit
): AddSubscriptionComponent, ComponentContext by componentContext {

    val coroutineScope = componentContext.coroutineScope()

    private val _listModules = MutableStateFlow<List<ModuleDTO>>(emptyList())
    override val listModules: StateFlow<List<ModuleDTO>> = _listModules

    override fun loadModules() {
        coroutineScope.launch {
            val response = apiClient.subscriptionsSA.getAllModules()
            if (response is ApiResponse.Success) {
                _listModules.value = response.data.toList()
            } else {
                _listModules.value = emptyList()
            }
            println("listModules: ${_listModules.value}")
        }
    }

    override fun addModule(module: ModuleDTO) {
        coroutineScope.launch {
            val response = apiClient.subscriptionsSA.createSubscription(company.id, module.id)
            if (response is ApiResponse.Success) {
                onFinished(true)
            } else {
                //todo: mostrar que hubo un error?
                onFinished(false)
            }
        }
    }

    init {
        loadModules()
    }
}