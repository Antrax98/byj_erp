package dev.byjtech.erp.document_management.application

import dev.byjtech.erp.document_management.application.service.DocumentService
import dev.byjtech.erp.document_management.application.service.DocumentEditHistoryService
import dev.byjtech.erp.document_management.application.service.DocumentAuditLogService
import org.koin.dsl.module

val documentManagementApplicationModule = module {
    single { DocumentEditHistoryService(get()) }
    single { DocumentService(get(), get(), get()) }
    single { DocumentAuditLogService() }
}
