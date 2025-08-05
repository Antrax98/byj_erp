package dev.byjtech.erp.machinery.infrastructure

import dev.byjtech.erp.machinery.domain.repository.MachineryRepository
import dev.byjtech.erp.machinery.domain.repository.OperationalDataRepository
import dev.byjtech.erp.machinery.domain.repository.MaintenanceScheduleRepository
import dev.byjtech.erp.machinery.domain.repository.WorkOrderRepository
import dev.byjtech.erp.machinery.domain.repository.MachineryDocumentRepository
import dev.byjtech.erp.machinery.domain.repository.MachineryNotificationRepository
import dev.byjtech.erp.machinery.infrastructure.api.MachineryRoutesInstaller
import dev.byjtech.erp.machinery.infrastructure.api.machinery.MachineryApiRoutesInstaller
import dev.byjtech.erp.machinery.infrastructure.exposed.repository.MachineryRepositoryImpl
import dev.byjtech.erp.machinery.infrastructure.exposed.repository.OperationalDataRepositoryImpl
import dev.byjtech.erp.machinery.infrastructure.exposed.repository.MaintenanceScheduleRepositoryImpl
import dev.byjtech.erp.machinery.infrastructure.exposed.repository.WorkOrderRepositoryImpl
import dev.byjtech.erp.machinery.infrastructure.exposed.repository.MachineryDocumentRepositoryImpl
import dev.byjtech.erp.machinery.infrastructure.exposed.repository.MachineryNotificationRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val machineryinfrastructureModule = module {
    // Repositories
    single<MachineryRepository> { MachineryRepositoryImpl(get(named("machineryDatabase"))) }
    single<OperationalDataRepository> { OperationalDataRepositoryImpl(get(named("machineryDatabase"))) }
    single<MaintenanceScheduleRepository> { MaintenanceScheduleRepositoryImpl(get(named("machineryDatabase"))) }
    single<WorkOrderRepository> { WorkOrderRepositoryImpl(get(named("machineryDatabase"))) }
    single<MachineryDocumentRepository> { MachineryDocumentRepositoryImpl(get(named("machineryDatabase"))) }
    single<MachineryNotificationRepository> { MachineryNotificationRepositoryImpl(get(named("machineryDatabase"))) }
    
    // Route Installers
    single<MachineryApiRoutesInstaller> { MachineryApiRoutesInstaller(get()) }
    
    // API
    single<MachineryRoutesInstaller> {
        MachineryRoutesInstaller(
            setOf(get<MachineryApiRoutesInstaller>())
        )
    }
}