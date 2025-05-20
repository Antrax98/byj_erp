package dev.byjtech.erp.shared.routing

import io.ktor.server.routing.Route

interface RoutesInstaller {
    fun Route.installRoutes()
}