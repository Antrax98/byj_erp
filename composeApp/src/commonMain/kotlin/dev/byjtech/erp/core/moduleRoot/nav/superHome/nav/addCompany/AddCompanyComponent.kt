package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany

import dev.byjtech.erp.common.api.ApiClient

interface AddCompanyComponent {
    val apiClient: ApiClient
    val onFinished: (added: Boolean) -> Unit
}