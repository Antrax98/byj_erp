package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.di

import dev.byjtech.erp.common.FeatureEntry
import dev.byjtech.erp.common.ModuleEntry
import org.koin.dsl.module
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.RolesFeatureEntry
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.test.testFeatureEntry
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.UsersFeatureEntry
import org.koin.core.qualifier.named


val CoreModule = module {
    //deprecado este single
//    single{
//        OldModuleEntry(
//            name = CoreDefinition.name,
//            factory = { context, userPermissions, apiClient, toHome ->
//                CoreAdminRootComponentImplOld(context, userPermissions, apiClient, toHome = toHome, coreClient = CoreClient(apiClient.ktorfit))
//            },
//            renderScreen = { component -> CoreAdminRootScreen(component as CoreAdminRootComponentOld) },
//            metadata = OldModuleMetadata(
//                displayName = CoreDefinition.displayName,
//                description = CoreDefinition.description,
//                iconPath = "#32a852"
//            )
//        )
//    }

    val featuresEntrySet = setOf<FeatureEntry>(
        // aqui poner todos los featureEntry uno por uno, asi:
        testFeatureEntry,
        UsersFeatureEntry,
        RolesFeatureEntry,
    )

    single<ModuleEntry>(named ("CoreEntry")){
        ModuleEntry(
            name = CoreDefinition.name,
            features = featuresEntrySet// aqui se mapea el nombre del featureEntry a si mismo
        )
    }

}