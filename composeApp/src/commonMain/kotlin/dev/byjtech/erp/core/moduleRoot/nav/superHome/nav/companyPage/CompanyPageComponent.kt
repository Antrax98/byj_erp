package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companyPage

import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.CompanyDTO
import dev.byjtech.erp.core.dto.ModuleDTO
import dev.byjtech.erp.core.dto.SubscriptionDTO
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeComponentImpl
import kotlinx.coroutines.flow.StateFlow

interface CompanyPageComponent {
    val company: CompanyDTO
    val apiClient: ApiClient
    val subsModMap: StateFlow<Map<SubscriptionDTO, ModuleDTO>>
    val navTo: (SuperHomeComponentImpl.Config) -> Unit
    fun navToAddSubscription(company: CompanyDTO)
    fun changeSubscriptionAccess(subscriptionId: String, isAllowed: Boolean)
}