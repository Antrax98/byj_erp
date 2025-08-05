package dev.byjtech.erp.modules.machinery

import dev.byjtech.erp.common.ModuleEntry
import dev.byjtech.erp.modules.machinery.di.machineryModule
import dev.byjtech.erp.modules.machinery.featureEntry.machineryListFeatureEntry
import org.koin.dsl.module

val MachineryModule = module {
    includes(machineryModule)
    
    single<ModuleEntry>(qualifier = org.koin.core.qualifier.named("MachineryEntry")) {
        ModuleEntry(
            name = "machinery",
            features = setOf(
                machineryListFeatureEntry
            )
        )
    }
}
