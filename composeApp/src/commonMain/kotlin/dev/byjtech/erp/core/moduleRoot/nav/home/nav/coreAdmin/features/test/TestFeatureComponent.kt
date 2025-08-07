package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.test

import dev.byjtech.erp.common.FeatureComponent
import kotlinx.coroutines.flow.StateFlow

interface TestFeatureComponent: FeatureComponent {
    val state: StateFlow<TestFeatureState>
}