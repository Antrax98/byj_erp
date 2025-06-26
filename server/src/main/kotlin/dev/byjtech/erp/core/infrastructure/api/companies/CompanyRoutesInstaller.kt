package dev.byjtech.erp.core.infrastructure.api.companies

import dev.byjtech.erp.core.domain.repository.CompanyRepository
import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.domain.repository.SubscriptionRepository
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import org.jetbrains.exposed.sql.Database

class CompanyRoutesInstaller(
    private val companyRepo: CompanyRepository,
    private val authServ: CoreAuthWrapper,
    private val userRepo: UserRepository,
    private val moduleRepo: ModuleRepository,
    private val subscriptionRepo: SubscriptionRepository,
): RoutesInstaller {
    override fun Route.installRoutes() {
        route("/companies/tenant") {
            //aqui van las rutas de la api tenant
        }
        route("/companies/super-admin") {
            superAdminCompanies(authServ, companyRepo, userRepo, moduleRepo, subscriptionRepo)
        }

    }
}