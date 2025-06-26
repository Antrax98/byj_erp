package dev.byjtech.erp.core.infrastructure.api

import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.shared.routing.ModuleRoutesInstaller
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.server.routing.Route
import io.ktor.server.routing.route

//funciona como un contenedor para asignarle los routeinstaller del modulo en si y darlelo al Initializer
// es por orden
//se le da los installer desde koin, pero igualmente se puede hacer desde aqui
class DocumentManagementRoutesInstaller(
    installers: Set<RoutesInstaller>
) : ModuleRoutesInstaller(
    installers
)