package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companyPage

import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.dto.CompanyDTO
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeComponentImpl

interface CompanyPageComponent {
    val company: CompanyDTO
    val apiClient: ApiClient
    val navTo: (SuperHomeComponentImpl.Config) -> Unit
}