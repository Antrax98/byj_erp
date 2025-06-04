package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.test

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.core.api.users.UsersTenantApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TestFeatureComponentImpl(
    val componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val usersTenantApi: UsersTenantApi,
    override val apiClient: ApiClient, //ojala no usar este de ser posible
    override val toHome: () -> Unit
): TestFeatureComponent, ComponentContext by componentContext {

    //si el feature tiene navegacion nesteada, no usar state en si mismo, solo en sus hijos????
    private val _state = MutableStateFlow(TestFeatureState())
    override val state: StateFlow<TestFeatureState> = _state.asStateFlow()

}