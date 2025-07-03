package dev.byjtech.erp.modules.document_management
import dev.byjtech.erp.config.ModuleInitializer
import dev.byjtech.erp.modules.document_management.domain.documentManagementDomainModule
import dev.byjtech.erp.modules.document_management.application.documentManagementApplicationModule
import dev.byjtech.erp.modules.document_management.infrastructure.documentManagementInfrastructureModule
import dev.byjtech.erp.document_management.infrastructure.api.DocumentManagementRoutesInstaller
import dev.byjtech.erp.modules.document_management.infrastructure.exposed.DocumentManagementTables
import io.github.cdimascio.dotenv.dotenv
import dev.byjtech.erp.shared.infrastructure.database.CreateDatabase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dotenv = dotenv {
    ignoreIfMissing = false
}

val documentManagementModule = module {
    includes(
        documentManagementDomainModule,
        documentManagementApplicationModule,
        documentManagementInfrastructureModule,
    )

    single(named("documentManagementDatabase")) {
    CreateDatabase(
        dotenv["DOCUMENT_DB_HOST"],
        dotenv["DOCUMENT_DB_PORT"],
        dotenv["DOCUMENT_DB_NAME"],
        dotenv["DOCUMENT_DB_USER"],
        dotenv["DOCUMENT_DB_PASSWORD"]
    )
}

    single<ModuleInitializer> {
        DocumentManagementInitializer(
            definition = DocumentManagementDefinition,
            database = get(named("documentManagementDatabase")),
            moduleRoutesInstaller = get<DocumentManagementRoutesInstaller>(),
            tables = DocumentManagementTables.all
        )
    }
}
