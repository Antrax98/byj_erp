package dev.byjtech.erp.document_management
import dev.byjtech.erp.shared.routing.ModuleRoutesInstaller
import dev.byjtech.erp.common.ModuleDefinition
import dev.byjtech.erp.config.ModuleInitializer
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table

class DocumentManagementInitializer(
    definition: ModuleDefinition,
    tables: Set<Table>,
    moduleRoutesInstaller: ModuleRoutesInstaller,
    database: Database,
) : ModuleInitializer(definition,tables,database,moduleRoutesInstaller)

