package dev.byjtech.erp.machinery.infrastructure

import dev.byjtech.erp.machinery.infrastructure.api.MachineryRoutesInstaller
import org.koin.dsl.module

val machineryinfrastructureModule = module {
    single<MachineryRoutesInstaller> {
        MachineryRoutesInstaller(
            emptySet()
        )
    }
}