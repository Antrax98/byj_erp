package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolesMain.RolesMainComponent

interface RolesFeatureComponent: FeatureComponent {

    val childStack: Value<ChildStack<*, Child>>

    //TODO(): cambiar despues
    sealed class Child {
        class RolesMain(val component: RolesMainComponent): Child()
    }
}