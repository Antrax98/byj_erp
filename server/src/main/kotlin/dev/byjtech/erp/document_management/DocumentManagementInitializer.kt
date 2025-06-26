package dev.byjtech.erp.modules.document_management

import dev.byjtech.erp.common.ModuleDefinition
import dev.byjtech.erp.config.ModuleInitializer
import dev.byjtech.erp.modules.document_management.infrastructure.exposed.DocumentManagementTables
import dev.byjtech.erp.modules.document_management.infrastructure.api.DocumentManagementRoutesInstaller
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table

class DocumentManagementInitializer(
    definition: ModuleDefinition,
    tables: Set<Table>,
    moduleRoutesInstaller: ModuleRoutesInstaller,
    database: Database,
) : ModuleInitializer(definition,tables,database,moduleRoutesInstaller)

