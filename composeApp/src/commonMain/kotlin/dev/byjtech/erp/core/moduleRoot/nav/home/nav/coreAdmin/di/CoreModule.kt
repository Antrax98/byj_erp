package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.di

import dev.byjtech.erp.common.ModuleEntry
import dev.byjtech.erp.common.ModuleMetadata
import dev.byjtech.erp.core.api.CoreClient
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.CoreAdminRootComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.CoreAdminRootComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.CoreAdminRootScreen
import org.koin.dsl.module
import dev.byjtech.erp.core.CoreDefinition




val CoreModule = module {

    single{
        ModuleEntry(
            name = CoreDefinition.name,
            factory = { context, userPermissions, apiClient, toHome ->
                CoreAdminRootComponentImpl(context, userPermissions, apiClient, toHome = toHome, coreClient = CoreClient(apiClient.ktorfit))
            },
            renderScreen = { component -> CoreAdminRootScreen(component as CoreAdminRootComponent) },
            metadata = ModuleMetadata(
                displayName = CoreDefinition.displayName,
                description = CoreDefinition.description,
                iconPath = "#32a852"
            )
        )
    }
    //aqui poner todos los "feature" del core (users, role, company, etc...)

}