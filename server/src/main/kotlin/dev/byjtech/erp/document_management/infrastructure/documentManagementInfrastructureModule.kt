package dev.byjtech.erp.document_management.infrastructure
import dev.byjtech.erp.document_management.domain.repository.*
import dev.byjtech.erp.document_management.infrastructure.auth.DocumentManagementAuthWrapper
import dev.byjtech.erp.document_management.infrastructure.exposed.repository.*
import dev.byjtech.erp.document_management.infrastructure.api.DocumentManagementRoutesInstaller
import dev.byjtech.erp.document_management.infrastructure.api.controllers.DocumentRoutesInstaller
import dev.byjtech.erp.document_management.infrastructure.api.controllers.DocumentEditHistoryRoutesInstaller
import org.koin.core.qualifier.named
import org.koin.dsl.module

val documentManagementInfrastructureModule = module {

    single<DocumentRepository> { DocumentRepositoryImpl(get(named("documentManagementDatabase"))) }
    single<DocumentEditHistoryRepository> { DocumentEditHistoryRepositoryImpl(get(named("documentManagementDatabase"))) }
    single<DocumentAuditLogRepository> { DocumentAuditLogRepositoryImpl(get(named("documentManagementDatabase"))) }
    
    // Repositorio para validar referencias cruzadas con la base de datos core
    single<CompanyValidationRepository> { CompanyValidationRepositoryImpl(get(named("coreDatabase"))) }

    // Auth wrapper para el módulo
    single<DocumentManagementAuthWrapper> {
        DocumentManagementAuthWrapper(get())
    }

    // Controladores de rutas
    single<DocumentRoutesInstaller> {
        DocumentRoutesInstaller(
            documentService = get(),
            authWrapper = get()
        )
    }

    single<DocumentEditHistoryRoutesInstaller> {
        DocumentEditHistoryRoutesInstaller(
            documentEditHistoryService = get(),
            authWrapper = get()
        )
    }

    single<DocumentManagementRoutesInstaller> {
        DocumentManagementRoutesInstaller(
            setOf(
                get<DocumentRoutesInstaller>(),
                get<DocumentEditHistoryRoutesInstaller>()
            )
        )
    }
}
