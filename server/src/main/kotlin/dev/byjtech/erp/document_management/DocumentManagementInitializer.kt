package dev.byjtech.erp.modules.document_management

import dev.byjtech.erp.common.ModuleDefinition
import dev.byjtech.erp.config.ModuleInitializer
import dev.byjtech.erp.modules.document_management.infrastructure.exposed.DocumentManagementTables
import dev.byjtech.erp.modules.document_management.routing.DocumentManagementRoutesInstaller
import dev.byjtech.erp.shared.routing.ModuleRoutesInstaller
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table

class DocumentManagementInitializer(
    definition: ModuleDefinition,
    database: Database,
    moduleRoutesInstaller: ModuleRoutesInstaller = DocumentManagementRoutesInstaller(),
) : ModuleInitializer(
    definition = definition,
    tables = DocumentManagementTables.all,
    database = database,
    moduleRoutesInstaller = moduleRoutesInstaller
)
