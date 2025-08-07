package dev.byjtech.erp.modules.machinery

import dev.byjtech.erp.common.ModuleDefinition
import dev.byjtech.erp.config.ModuleInitializer
import dev.byjtech.erp.shared.routing.ModuleRoutesInstaller
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table


class MachineryInitializer(
    definition: ModuleDefinition,
    tables: Set<Table>,
    moduleRoutesInstaller: ModuleRoutesInstaller,
    database: Database,
): ModuleInitializer(definition,tables,database,moduleRoutesInstaller)