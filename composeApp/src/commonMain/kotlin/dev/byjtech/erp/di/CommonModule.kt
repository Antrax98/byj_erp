package dev.byjtech.erp.di

import dev.byjtech.erp.common.old.OldModuleManager
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.di.CoreModule
import org.koin.dsl.module
import org.koin.core.module.Module

expect val platformModule: Module

val commonModule = module {
    includes(
        CoreModule //este lo cree yo
    )
    single<OldModuleManager> {
        OldModuleManager(modules = getAll())
    }
}