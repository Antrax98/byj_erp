package dev.byjtech.erp.modules.machinery.di

import org.koin.dsl.module

val machineryModule = module {
    // No necesitamos registrar MachineryApi en Koin
    // Se creará directamente en el componente usando apiClient.clientKtor
}