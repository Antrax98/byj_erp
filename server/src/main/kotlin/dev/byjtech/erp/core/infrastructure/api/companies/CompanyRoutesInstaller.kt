package dev.byjtech.erp.core.infrastructure.api.companies

import dev.byjtech.erp.core.domain.repository.CompanyRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.server.routing.Route
import io.ktor.server.routing.route

class CompanyRoutesInstaller(
    private val companyRepo: CompanyRepository,
    private val authServ: CoreAuthWrapper

): RoutesInstaller {
    override fun Route.installRoutes() {
        route("/companies/tenant") {
            //aqui van las rutas de la api tenant
        }
        route("/companies/super-admin") {
            superAdminCompanies(authServ, companyRepo)
        }

    }
}