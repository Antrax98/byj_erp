package dev.byjtech.erp.core.moduleRoot.nav.superHome

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.*
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager
import dev.byjtech.erp.common.PermissionKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.core.dto.CompanyDTO
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany.AddCompanyComponent
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany.AddCompanyComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companies.CompaniesComponent
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companies.CompaniesComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companyPage.CompanyPageComponent
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companyPage.CompanyPageComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.superHomeMain.SuperHomeMainComponent
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.superHomeMain.SuperHomeMainComponentImpl
import dev.byjtech.erp.modules.document_management.features.documents.DocumentsFeatureComponent
import dev.byjtech.erp.modules.document_management.features.documents.DocumentsFeatureComponentImpl
import kotlinx.coroutines.launch

class SuperHomeComponentImpl (
    componentContext: ComponentContext,
    private val sessionManager: SessionManager,
    private val api: ApiClient
): SuperHomeComponent, ComponentContext by componentContext {

    private val coroutineScope = componentContext.coroutineScope()

    override fun onLogout() {
        coroutineScope.launch {
            sessionManager.logout()
        }
    }

    override fun onTestClick() {
        coroutineScope.launch {
            api.coreAuth.test()
        }
    }

    private val _state = MutableStateFlow(SuperHomeState())
    override val state: StateFlow<SuperHomeState> = _state.asStateFlow()

    private val _isOnMainPage = MutableStateFlow(true)
    override val isOnMainPage: StateFlow<Boolean> = _isOnMainPage.asStateFlow()

    // StateFlow vacío para user permissions - en SuperHome no necesitamos permisos específicos
    private val _userPermissions = MutableStateFlow<Set<PermissionKey>>(emptySet())
    private val userPermissions: StateFlow<Set<PermissionKey>> = _userPermissions.asStateFlow()

    //navegacion
    @Serializable
    sealed class Config {
        @Serializable
        data object Companies : Config()
        @Serializable
        data object Main : Config()
        @Serializable
        data object AddCompany : Config()
        @Serializable
        data class CompanyPage(val company: CompanyDTO) : Config()
        @Serializable
        data object DocumentsFeature : Config()
    }

    private val navigation = StackNavigation<Config>()

    private val stack  = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Main) },
        handleBackButton = true,
        childFactory = ::childFactory
    )

    override val childStack: Value<ChildStack<*, SuperHomeComponent.Child>> = stack

    private fun companiesComponent(componentContext: ComponentContext): CompaniesComponent =
        CompaniesComponentImpl(componentContext, api, ::navTo)

    private fun mainComponent(componentContext: ComponentContext): SuperHomeMainComponent =
        SuperHomeMainComponentImpl(componentContext, api, ::navTo)

    private fun addCompanyComponent(componentContext: ComponentContext): AddCompanyComponent =
        AddCompanyComponentImpl(componentContext, api){
            added ->
            println("added: $added")
            println("terminar funcion")
            navigation.pop()
        }

    private fun companyPageComponent(componentContext: ComponentContext, company: CompanyDTO): CompanyPageComponent =
        CompanyPageComponentImpl(componentContext, company, api, ::navTo)

    private fun documentsFeatureComponent(componentContext: ComponentContext): DocumentsFeatureComponent =
        DocumentsFeatureComponentImpl(
            componentContext = componentContext,
            userPermissions = userPermissions,
            apiClient = api,
            toHome = {
                navigation.popTo(0)
            },
            updateTitle = { title ->
                // En SuperHome no actualizamos título, ya que es la pantalla principal
            }
        )

    private fun childFactory(config: Config, componentContext: ComponentContext): SuperHomeComponent.Child {
        return when (config) {
            is Config.Companies -> SuperHomeComponent.Child.Companies(companiesComponent(componentContext.childContext("companies")))
            is Config.Main -> SuperHomeComponent.Child.Main(mainComponent(componentContext.childContext("main-page")))
            is Config.AddCompany -> SuperHomeComponent.Child.AddCompany(addCompanyComponent(componentContext.childContext("add-company")))
            is Config.CompanyPage -> SuperHomeComponent.Child.CompanyPage(companyPageComponent(componentContext.childContext("company-page"), config.company))
            is Config.DocumentsFeature -> SuperHomeComponent.Child.DocumentsFeature(documentsFeatureComponent(componentContext.childContext("documents-feature")))
        }
    }

    private fun navTo(config: Config) {
        val current = childStack.value.active.configuration
        if (current != config) {
            _isOnMainPage.value = false
            navigation.pushNew(config)
        }
    }

    private fun toHome() {
        _isOnMainPage.value = true
        navigation.popToFirst()
    }

    override fun onBack() {
        navigation.pop{
            if (childStack.active.configuration == Config.Main){
                _isOnMainPage.value = true
            }
        }
    }

}