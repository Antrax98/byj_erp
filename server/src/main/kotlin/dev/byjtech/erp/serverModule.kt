package dev.byjtech.erp

import dev.byjtech.erp.shared.infrastructure.database.databaseModule
import org.koin.dsl.module

val serverModule = module {
    includes(
        *allModules.toTypedArray(),
        databaseModule
    )


}