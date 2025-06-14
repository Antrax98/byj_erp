package dev.byjtech.erp

import org.koin.dsl.module

val serverModule = module {
    includes(
        *allModules.toTypedArray(),
    )

}