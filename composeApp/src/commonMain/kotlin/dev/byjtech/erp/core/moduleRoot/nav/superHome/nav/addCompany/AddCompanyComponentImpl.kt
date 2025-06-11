package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.api.ApiClient

class AddCompanyComponentImpl(
    componentContext: ComponentContext,
    override val apiClient: ApiClient,
    override val onFinished: (added: Boolean) -> Unit
): AddCompanyComponent, ComponentContext by componentContext {
}