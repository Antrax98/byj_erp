package dev.byjtech.erp.shared.routing

import io.ktor.server.routing.Route

//funciona mas como un contenedor que otra cosa
abstract class ModuleRoutesInstaller(
    private val installers: Set<RoutesInstaller>
) {
    fun Route.installRoutes() {
        installers.forEach { installer ->
            with(installer) {
                this@installRoutes.installRoutes()
            }
        }
    }
}