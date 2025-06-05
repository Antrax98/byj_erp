package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.di

import dev.byjtech.erp.common.FeatureEntry
import dev.byjtech.erp.common.ModuleEntry
import dev.byjtech.erp.common.old.OldModuleEntry
import dev.byjtech.erp.common.old.OldModuleMetadata
import dev.byjtech.erp.core.api.CoreClient
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.CoreAdminRootComponentOld
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.CoreAdminRootComponentImplOld
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.CoreAdminRootScreen
import org.koin.dsl.module
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.test.testFeatureEntry
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.UsersFeatureEntry


val CoreModule = module {
    //deprecado este single
    single{
        OldModuleEntry(
            name = CoreDefinition.name,
            factory = { context, userPermissions, apiClient, toHome ->
                CoreAdminRootComponentImplOld(context, userPermissions, apiClient, toHome = toHome, coreClient = CoreClient(apiClient.ktorfit))
            },
            renderScreen = { component -> CoreAdminRootScreen(component as CoreAdminRootComponentOld) },
            metadata = OldModuleMetadata(
                displayName = CoreDefinition.displayName,
                description = CoreDefinition.description,
                iconPath = "#32a852"
            )
        )
    }

    val featuresEntrySet = setOf<FeatureEntry>(
        // aqui poner todos los featureEntry uno por uno, asi:
        //usersFeature,
        //roleFeature,
        //etc.....
        testFeatureEntry,
        UsersFeatureEntry
    )

    single{
        ModuleEntry(
            name = CoreDefinition.name,
            features = featuresEntrySet// aqui se mapea el nombre del featureEntry a si mismo
        )
    }

}