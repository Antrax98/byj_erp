package dev.byjtech.erp.core.infrastructure.api.companies

import dev.byjtech.erp.core.domain.repository.CompanyRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.common.ApiResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toDTO

fun Route.superAdminCompanies(authServ: CoreAuthWrapper, companyRepo: CompanyRepository) {
    get("/all-companies"){
        authServ.authorizeOrThrow(call, requiredSuperAdmin = true)

        val companies = companyRepo.findAll().map { it.toDTO() }.toSet()

        call.respond(HttpStatusCode.OK, companies)

    }
}