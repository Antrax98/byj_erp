package dev.byjtech.erp.modules.machinery.application

import dev.byjtech.erp.modules.machinery.application.service.MachineryService
import dev.byjtech.erp.modules.machinery.application.service.MachineryHistoryService
import org.koin.dsl.module

val machineryapplicationModule = module {
    single<MachineryService> {
        MachineryService(
            get(),
            get()
        )
    }
    single<MachineryHistoryService> { MachineryHistoryService(get()) }
}