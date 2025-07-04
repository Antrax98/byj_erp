package dev.byjtech.erp.document_management.infrastructure
import dev.byjtech.erp.document_management.domain.repository.*
import dev.byjtech.erp.document_management.infrastructure.exposed.repository.*
import dev.byjtech.erp.document_management.infrastructure.api.DocumentManagementRoutesInstaller
import org.koin.core.qualifier.named
import org.koin.dsl.module

val documentManagementInfrastructureModule = module {

    single<DocumentRepository> { DocumentRepositoryImpl(get(named("documentDatabase"))) }
    single<DocumentEditHistoryRepository> { DocumentEditHistoryRepositoryImpl(get(named("documentDatabase"))) }
    single<DocumentAuditLogRepository> { DocumentAuditLogRepositoryImpl(get(named("documentDatabase"))) }

    single<DocumentManagementRoutesInstaller> {
        DocumentManagementRoutesInstaller(
            documentService = get(),
            editHistoryService = get(),
            auditLogService = get()
        )
    }
}
