package dev.byjtech.erp

import dev.byjtech.erp.core.coreModule
import dev.byjtech.erp.modules.moduleTest.testModule

val allModules = setOf(
    coreModule,
    //testModule,
    //TODO(): aqui agregar los modulos koin de cada moduloERP
    //hrModule,
    //storageModule,
)