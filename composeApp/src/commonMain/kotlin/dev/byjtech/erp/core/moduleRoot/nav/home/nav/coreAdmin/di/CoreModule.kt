package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.di

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import dev.byjtech.erp.common.ModuleEntry
import dev.byjtech.erp.common.ModuleMetadata
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.CoreAdminRootComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.CoreAdminRootComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.CoreAdminRootScreen
import org.koin.dsl.module





val CoreModule = module {

    single{
        ModuleEntry(
            name = "core",
            factory = { context, sessionManager, apiClient, toHome ->
                CoreAdminRootComponentImpl(context, sessionManager, apiClient, toHome = toHome)
            },
            renderScreen = { component -> CoreAdminRootScreen(component as CoreAdminRootComponent) },
            metadata = ModuleMetadata(
                displayName = "Core",
                description = "Core module",
                iconPath = "#32a852"
            )
        )
    }

}