package dev.byjtech.erp.core.infrastructure.api.users

import dev.byjtech.erp.core.application.service.UserService
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.server.routing.Route
import io.ktor.server.routing.route

class UserRoutesInstaller (
    private val userServ: UserService
) : RoutesInstaller {
    override fun Route.installRoutes() {
        route("/tenant") {
            tenantUsers()
        }
        route("/superadmin") {
            superAdminUsers()
        }
    }
}