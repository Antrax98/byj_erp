package dev.byjtech.erp

import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.modules.machinery.MachineryDefinition
import dev.byjtech.erp.modules.TestDefinition
import org.koin.dsl.module

val sharedModule = module {
    single<CoreDefinition>{ CoreDefinition }
    single<TestDefinition>{ TestDefinition }
    single<MachineryDefinition>{ MachineryDefinition }
}