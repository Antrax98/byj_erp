package dev.byjtech.erp.core.moduleRoot.nav.superHome

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany.AddCompanyComponent
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addSubscription.AddSubscriptionComponent
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companies.CompaniesComponent
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companyPage.CompanyPageComponent
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.superHomeMain.SuperHomeMainComponent
import dev.byjtech.erp.modules.document_management.features.documents.DocumentsFeatureComponent
import dev.byjtech.erp.modules.document_management.features.editHistory.EditHistoryFeatureComponent
import dev.byjtech.erp.modules.document_management.features.auditLogs.AuditLogsFeatureComponent
import kotlinx.coroutines.flow.StateFlow

interface SuperHomeComponent {
    val state: StateFlow<SuperHomeState>
    val isOnMainPage: StateFlow<Boolean>
    val childStack: Value<ChildStack<*, Child>>
    fun onBack()
    fun onLogout()
    fun onTestClick()

    sealed class Child {
        class Companies(val component: CompaniesComponent) : Child()
        class Main(val component: SuperHomeMainComponent) : Child()
        class AddCompany(val component: AddCompanyComponent) : Child()
        class CompanyPage(val component: CompanyPageComponent) : Child()
        class DocumentsFeature(val component: DocumentsFeatureComponent) : Child()
        class EditHistoryFeature(val component: EditHistoryFeatureComponent) : Child()
        class AuditLogsFeature(val component: AuditLogsFeatureComponent) : Child()
        class AddSubscription(val component: AddSubscriptionComponent) : Child()
    }
}