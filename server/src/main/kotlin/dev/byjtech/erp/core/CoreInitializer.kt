package dev.byjtech.erp.core

import dev.byjtech.erp.common.ModuleDefinition
import dev.byjtech.erp.config.ModuleInitializer
import dev.byjtech.erp.shared.routing.ModuleRoutesInstaller
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.server.routing.Route
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table

//solo coreDefinition tiene authRoutesInstaller, ya que estas rutas son especiales, los demas modulos solo tienen moduleRoutesInstaller
class CoreInitializer(
    definition: ModuleDefinition,
    tables: Set<Table>,
    moduleRoutesInstaller: ModuleRoutesInstaller,
    database: Database,
    private val authRoutesInstaller: RoutesInstaller,
): ModuleInitializer(definition,tables,database,moduleRoutesInstaller) {
    //este caso es especial, no referenciar esta funcion
    fun Route.installAuthRoutes() {
        with(authRoutesInstaller) {
            this@installAuthRoutes.installRoutes()
        }
    }
}
