package dev.byjtech.erp.machinery.application

import dev.byjtech.erp.machinery.application.service.MachineryService
import org.koin.dsl.module

val machineryapplicationModule = module {
    single<MachineryService> { MachineryService(get()) }
}