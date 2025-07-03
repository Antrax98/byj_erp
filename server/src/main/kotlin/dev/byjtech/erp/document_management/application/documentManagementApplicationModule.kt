package dev.byjtech.erp.modules.document_management.application

import dev.byjtech.erp.modules.document_management.application.service.DocumentService
import dev.byjtech.erp.modules.document_management.application.service.DocumentEditHistoryService
import dev.byjtech.erp.modules.document_management.application.service.DocumentAuditLogService
import org.koin.dsl.module

val documentManagementApplicationModule = module {
    single { DocumentService(get()) }
    single { DocumentEditHistoryService(get()) }
    single { DocumentAuditLogService(get()) }
}
