package dev.byjtech.erp.core.infrastructure.api.roles

import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.domain.repository.RoleRepository
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.server.routing.Route
import io.ktor.server.routing.route

class RoleRoutesInstaller(
    private val roleRepo: RoleRepository,
    private val authServ: CoreAuthWrapper,
    private val userRepo: UserRepository,
    private val moduleRepo: ModuleRepository
    ): RoutesInstaller {
    override fun Route.installRoutes() {
        route("/roles") {
            route("/tenant") {
                tenantRoles(authServ, roleRepo, userRepo, moduleRepo)
            }
            route("/super-admin") {
                //placeholder
            }

        }
    }
}