package dev.byjtech.erp.config

import dev.byjtech.erp.common.ModuleDefinition
import dev.byjtech.erp.shared.routing.ModuleRoutesInstaller
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table

abstract class ModuleInitializer(
    val definition: ModuleDefinition,
    val tables: Set<Table>,
    val database: Database?,
    private val moduleRoutesInstaller: ModuleRoutesInstaller,
) {

    fun Route.installRoutes() {
        route("/${definition.name}") {
            with(moduleRoutesInstaller) {
                this@route.installRoutes()
            }
        }
    }
}