package dev.byjtech.erp.document_management
import dev.byjtech.erp.shared.routing.ModuleRoutesInstaller
import dev.byjtech.erp.common.ModuleDefinition
import dev.byjtech.erp.config.ModuleInitializer
import dev.byjtech.erp.document_management.application.service.DocumentExpirationScheduler
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table

class DocumentManagementInitializer(
    definition: ModuleDefinition,
    tables: Set<Table>,
    moduleRoutesInstaller: ModuleRoutesInstaller,
    database: Database,
    private val scheduler: DocumentExpirationScheduler
) : ModuleInitializer(definition,tables,database,moduleRoutesInstaller) {

    init {
        // Iniciar el scheduler de notificaciones después de la construcción
        scheduler.start()
    }
}

