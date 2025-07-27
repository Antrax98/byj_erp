package dev.byjtech.erp.core.infrastructure.api.subscriptions

import dev.byjtech.erp.core.domain.repository.CompanyRepository
import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.domain.repository.SubscriptionRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.server.routing.Route
import io.ktor.server.routing.route

class SubscriptionRoutesInstaller(
    private val authServ: CoreAuthWrapper,
    private val subscriptionRepo: SubscriptionRepository,
    private val moduleRepo: ModuleRepository,
    private val companyRepo: CompanyRepository
): RoutesInstaller {
    override fun Route.installRoutes() {
        route("/subscriptions") {
            route("/tenant") {
                //placeholder
            }
            route("/super-admin") {
                superAdminSubscriptions(authServ, subscriptionRepo, moduleRepo, companyRepo)
            }
        }
    }
}