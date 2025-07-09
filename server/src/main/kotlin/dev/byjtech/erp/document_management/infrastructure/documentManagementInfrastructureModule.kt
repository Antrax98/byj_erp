package dev.byjtech.erp.document_management.infrastructure
import dev.byjtech.erp.core.infrastructure.auth.AuthServiceContractImpl
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.document_management.domain.repository.*
import dev.byjtech.erp.document_management.infrastructure.exposed.repository.*
import dev.byjtech.erp.document_management.infrastructure.api.DocumentManagementRoutesInstaller
import org.koin.core.qualifier.named
import org.koin.dsl.module

val documentManagementInfrastructureModule = module {

    single<DocumentRepository> { DocumentRepositoryImpl(get(named("documentManagementDatabase"))) }
    single<DocumentEditHistoryRepository> { DocumentEditHistoryRepositoryImpl(get(named("documentManagementDatabase"))) }
    single<DocumentAuditLogRepository> { DocumentAuditLogRepositoryImpl(get(named("documentManagementDatabase"))) }

    single<DocumentManagementRoutesInstaller> {
        DocumentManagementRoutesInstaller(
            emptySet()
        )
    }
}

//authWraper  single<CoreAuthWrapper> { CoreAuthWrapper(get()) }  tengo que crearlo despues, propio modulo
