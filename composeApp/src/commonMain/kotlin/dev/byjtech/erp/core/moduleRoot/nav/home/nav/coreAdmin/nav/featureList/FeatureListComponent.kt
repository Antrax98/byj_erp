package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.nav.featureList

import dev.byjtech.erp.common.PermissionAwareComponent

interface FeatureListComponent: PermissionAwareComponent {
    val navTo: (String) -> Unit
}