package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addSubscription

import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.CompanyDTO
import dev.byjtech.erp.core.dto.ModuleDTO
import kotlinx.coroutines.flow.StateFlow

interface AddSubscriptionComponent {
    val apiClient: ApiClient
    val company: CompanyDTO
    val listModules: StateFlow<List<ModuleDTO>>
    val subModules: Set<ModuleDTO>
    val onFinished: (added: Boolean) -> Unit
    fun loadModules()
    fun addModule(module: ModuleDTO)
}