package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.featuresList

import dev.byjtech.erp.common.ButtonMetadata
import dev.byjtech.erp.common.ComponentConfig
import dev.byjtech.erp.common.FeatureComponent
import kotlinx.coroutines.flow.StateFlow

interface FeatureListComponent: FeatureComponent {
    val state: StateFlow<FeatureListState>
    val buttonsMap: Map<String, Map<String, ButtonMetadata>>
    val navTo: (ComponentConfig) -> Unit
}