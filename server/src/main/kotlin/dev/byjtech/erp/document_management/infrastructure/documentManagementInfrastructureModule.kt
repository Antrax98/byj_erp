package dev.byjtech.erp.document_management.infrastructure
import dev.byjtech.erp.document_management.domain.repository.*
import dev.byjtech.erp.document_management.infrastructure.exposed.repository.*
import dev.byjtech.erp.document_management.infrastructure.api.DocumentManagementRoutesInstaller
import dev.byjtech.erp.document_management.infrastructure.api.controllers.DocumentRoutesInstaller
import org.koin.core.qualifier.named
import org.koin.dsl.module

val documentManagementInfrastructureModule = module {

    single<DocumentRepository> { DocumentRepositoryImpl(get(named("documentManagementDatabase"))) }
    single<DocumentEditHistoryRepository> { DocumentEditHistoryRepositoryImpl(get(named("documentManagementDatabase"))) }
    single<DocumentAuditLogRepository> { DocumentAuditLogRepositoryImpl(get(named("documentManagementDatabase"))) }

    // Controladores de rutas
    single<DocumentRoutesInstaller> {
        DocumentRoutesInstaller(
            documentRepo = get()
        )
    }

    single<DocumentManagementRoutesInstaller> {
        DocumentManagementRoutesInstaller(
            setOf(
                get<DocumentRoutesInstaller>()
            )
        )
    }
}
