package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companyPage

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.CompanyDTO
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeComponentImpl

class CompanyPageComponentImpl(
    componentContext: ComponentContext,
    override val company: CompanyDTO,
    override val apiClient: ApiClient,
    override val navTo: (SuperHomeComponentImpl.Config) -> Unit
): CompanyPageComponent, ComponentContext by componentContext {
}