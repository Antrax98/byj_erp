package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.test

import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.core.api.users.UsersTenantApi
import kotlinx.coroutines.flow.StateFlow

interface TestFeatureComponent: FeatureComponent {
    val state: StateFlow<TestFeatureState>
    val usersTenantApi: UsersTenantApi //solo para testear si funciona este enfoque
}