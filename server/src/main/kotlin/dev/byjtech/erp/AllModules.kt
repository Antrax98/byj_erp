package dev.byjtech.erp

import dev.byjtech.erp.core.coreModule
import dev.byjtech.erp.document_management.documentManagementModule

val allModules = setOf(
    coreModule,
    documentManagementModule,
    //TODO(): aqui agregar los modulos koin de cada moduloERP
    //hrModule,
    //storageModule,
)