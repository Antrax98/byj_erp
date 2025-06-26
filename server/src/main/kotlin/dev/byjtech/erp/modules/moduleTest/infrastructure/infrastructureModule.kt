package dev.byjtech.erp.modules.moduleTest.infrastructure

import dev.byjtech.erp.modules.moduleTest.infrastructure.api.TestRoutesInstaller
import org.koin.dsl.module

val infrastructureModule = module {


    single<TestRoutesInstaller> {
        TestRoutesInstaller(
            emptySet()
        )
    }
}