package dev.byjtech.erp.di

import dev.byjtech.erp.common.ModuleEntry
import dev.byjtech.erp.common.old.OldModuleManager
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.di.CoreModule
import dev.byjtech.erp.modules.document_management.di.DocumentManagementModule
import org.koin.dsl.module
import org.koin.core.module.Module
import org.koin.core.qualifier.named

expect val platformModule: Module

val commonModule = module {
    includes(
        CoreModule //este lo cree yo
        DocumentManagementModule
    )

    single<List<ModuleEntry>>{
        listOf(
            get(named("CoreEntry")),
            //aqui van los demas modulesEntry
        )
    }
        
    single<OldModuleManager> {
        OldModuleManager(modules = getAll())
    }
}