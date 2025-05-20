package dev.byjtech.erp.core.infrastructure.api

import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.shared.routing.ModuleRoutesInstaller
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.server.routing.Route
import io.ktor.server.routing.route

//funciona como un contenedor para asignarle los routeinstaller del modulo en si y darlelo al Initializer
// es por orden
class CoreRoutesInstaller(
    installers: Set<RoutesInstaller>
) : ModuleRoutesInstaller(
    installers
) {
//    private val moduleName = CoreDefinition.name
//
//    override fun Route.installRoutes() {
//        installers.forEach(
//
//        )
//        baseRoute.route("/$moduleName") {
//            installers.forEach { it.installRoutes(this) }
//            CoreRoutes()
//        }
//    }
}