package dev.byjtech.erp.di 

import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    //aqui se agregan todas las dependencias exclusivas de android
}