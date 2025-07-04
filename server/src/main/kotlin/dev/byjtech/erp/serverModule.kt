package dev.byjtech.erp

import dev.byjtech.erp.config.ModuleInitializer
import org.koin.core.qualifier.named
import org.koin.dsl.module

val serverModule = module {
    includes(
        *allModules.toTypedArray(),
    )

    single<Set<ModuleInitializer>>(named("allInit")) {
        setOf(
            get(named("coreInit")),
            get(named("documentManagementInit")),
            //get(named("testInit"))
        )
    }

}