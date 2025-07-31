package dev.byjtech.erp.document_management.application

import dev.byjtech.erp.document_management.application.service.DocumentService
import dev.byjtech.erp.document_management.application.service.DocumentEditHistoryService
import dev.byjtech.erp.document_management.application.service.DocumentAuditLogService
import dev.byjtech.erp.document_management.application.service.NotificationService
import dev.byjtech.erp.document_management.application.service.DocumentExpirationScheduler
import org.koin.dsl.module

val documentManagementApplicationModule = module {
    single { DocumentEditHistoryService(get()) }
    single { DocumentAuditLogService(get()) }
    single { DocumentService(get(), get(), get(), get()) }
    single { NotificationService(get(), get(), get()) }
    single { DocumentExpirationScheduler(get(), get()) }
}
