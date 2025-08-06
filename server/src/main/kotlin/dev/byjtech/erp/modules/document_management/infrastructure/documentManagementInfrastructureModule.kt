package dev.byjtech.erp.document_management.infrastructure
import dev.byjtech.erp.document_management.domain.repository.*
import dev.byjtech.erp.document_management.infrastructure.auth.DocumentManagementAuthWrapper
import dev.byjtech.erp.document_management.infrastructure.exposed.repository.*
import dev.byjtech.erp.document_management.infrastructure.api.DocumentManagementRoutesInstaller
import dev.byjtech.erp.document_management.infrastructure.api.controllers.DocumentRoutesInstaller
import dev.byjtech.erp.document_management.infrastructure.api.controllers.DocumentEditHistoryRoutesInstaller
import dev.byjtech.erp.document_management.infrastructure.api.controllers.DocumentHistoryRoutesInstaller
import dev.byjtech.erp.document_management.infrastructure.api.controllers.NotificationRoutesInstaller
import org.koin.core.qualifier.named
import org.koin.dsl.module

val documentManagementInfrastructureModule = module {

    single<DocumentRepository> { DocumentRepositoryImpl(get(named("documentManagementDatabase"))) }
    single<DocumentEditHistoryRepository> { DocumentEditHistoryRepositoryImpl(get(named("documentManagementDatabase"))) }
    single<DocumentAuditLogRepository> { DocumentAuditLogRepositoryImpl(get(named("documentManagementDatabase"))) }
    single<UserNotificationSettingsRepository> { UserNotificationSettingsRepositoryImpl(get(named("documentManagementDatabase"))) }
    single<DocumentNotificationRepository> { DocumentNotificationRepositoryImpl(get(named("documentManagementDatabase"))) }
    
    single<CompanyValidationRepository> { CompanyValidationRepositoryImpl(get(named("coreDatabase"))) }

    single<DocumentManagementAuthWrapper> {
        DocumentManagementAuthWrapper(get())
    }

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

    single<DocumentHistoryRoutesInstaller> {
        DocumentHistoryRoutesInstaller(
            documentEditHistoryService = get(),
            documentAuditLogService = get(),
            authWrapper = get()
        )
    }

    single<NotificationRoutesInstaller> {
        NotificationRoutesInstaller(
            notificationService = get(),
            authWrapper = get()
        )
    }

    single<DocumentManagementRoutesInstaller> {
        DocumentManagementRoutesInstaller(
            setOf(
                get<DocumentRoutesInstaller>(),
                get<DocumentEditHistoryRoutesInstaller>(),
                get<DocumentHistoryRoutesInstaller>(),
                get<NotificationRoutesInstaller>()
            )
        )
    }
}
