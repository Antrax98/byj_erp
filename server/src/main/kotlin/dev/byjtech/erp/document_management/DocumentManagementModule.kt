package dev.byjtech.erp.document_management

import dev.byjtech.erp.config.ModuleInitializer
import dev.byjtech.erp.document_management.domain.documentManagementDomainModule
import dev.byjtech.erp.document_management.application.documentManagementApplicationModule
import dev.byjtech.erp.document_management.infrastructure.documentManagementInfrastructureModule
import dev.byjtech.erp.document_management.infrastructure.api.DocumentManagementRoutesInstaller
import dev.byjtech.erp.document_management.infrastructure.exposed.DocumentManagementTables
import io.github.cdimascio.dotenv.dotenv
import dev.byjtech.erp.shared.infrastructure.database.CreateDatabase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dotenv = dotenv {
    ignoreIfMissing = true
}

val documentManagementModule = module {
    includes(
        documentManagementDomainModule,
        documentManagementApplicationModule,
        documentManagementInfrastructureModule,
    )

    single(named("documentManagementDatabase")) {
        CreateDatabase(
            dotenv["DOCUMENT_DB_HOST"] ?: "localhost",
            dotenv["DOCUMENT_DB_PORT"] ?: "3306", 
            dotenv["DOCUMENT_DB_NAME"] ?: "documentos_db",
            dotenv["DOCUMENT_DB_USER"] ?: "root",
            dotenv["DOCUMENT_DB_PASSWORD"] ?: ""
        )
    }

    single<ModuleInitializer>(named("documentManagementInit")){
        DocumentManagementInitializer(
            definition = DocumentManagementDefinition,
            database = get(named("documentManagementDatabase")),
            moduleRoutesInstaller = get<DocumentManagementRoutesInstaller>(),
            tables = DocumentManagementTables.all
        )
    }

}
