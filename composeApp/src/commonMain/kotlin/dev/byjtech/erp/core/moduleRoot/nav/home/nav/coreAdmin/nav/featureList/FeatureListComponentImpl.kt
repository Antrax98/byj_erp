package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.nav.featureList

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.PermissionKey

class FeatureListComponentImpl(
    componentContext: ComponentContext,
    override val navTo: (String) -> Unit,
): FeatureListComponent, ComponentContext by componentContext {
    override val requiredPermissions: Set<PermissionKey> = emptySet()
    override val optionalPermissions: Set<PermissionKey> = emptySet()


}