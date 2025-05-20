package dev.byjtech.erp.core

import dev.byjtech.erp.common.ModuleDefinition
import dev.byjtech.erp.config.ModuleInitializer
import dev.byjtech.erp.shared.routing.ModuleRoutesInstaller
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.server.routing.Route
import org.jetbrains.exposed.sql.Table

//solo coreDefinition tiene authRoutesInstaller, ya que estas rutas son especiales, los demas modulos solo tienen moduleRoutesInstaller
class CoreInitializer(
    definition: ModuleDefinition,
    tables: Set<Table>,
    moduleRoutesInstaller: ModuleRoutesInstaller,
    private val authRoutesInstaller: RoutesInstaller,
): ModuleInitializer(definition,tables,moduleRoutesInstaller) {
    //este se ejecuta dentro de la ruta .../api para que quede ordenado
//    override fun installRoutes(baseRoute: Route) {
//        moduleRoutesInstaller.installRoutes(baseRoute)
//    }

    //este caso especial se ejecuta dentro de la ruta Raiz pero se podria hacer /auth o algo asi
    fun Route.installAuthRoutes() {
        with(authRoutesInstaller) {
            this@installAuthRoutes.installRoutes()
        }
    }

//    override fun Route.install(){
//        route("/${definition.name}"){
//            moduleRoutesInstaller.installRoutes(this)
//        }
//    }

}
