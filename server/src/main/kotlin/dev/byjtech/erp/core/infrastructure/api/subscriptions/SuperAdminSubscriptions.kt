package dev.byjtech.erp.core.infrastructure.api.subscriptions

import dev.byjtech.erp.core.domain.model.Module
import dev.byjtech.erp.core.domain.model.Subscription
import dev.byjtech.erp.core.domain.repository.CompanyRepository
import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.domain.repository.SubscriptionRepository
import dev.byjtech.erp.core.dto.ModuleDTO
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toDTO
import dev.byjtech.erp.core.response.SubscriptionsModResponse
import io.ktor.server.routing.Route
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import java.util.UUID
import io.ktor.http.HttpStatusCode

fun Route.superAdminSubscriptions(authServ: CoreAuthWrapper, subRepo: SubscriptionRepository, modRepo: ModuleRepository, companyRepo: CompanyRepository) {
    get("/company-subscriptions/{companyId}"){
        val session = authServ.authorizeOrThrow(
            call,
            requiredSuperAdmin = true
        )
        val companyId = call.parameters["companyId"]
        if (companyId == null) {
            call.respond(HttpStatusCode.BadRequest, message = "NO_COMPANY_ID")
            return@get
        }

        val companySubs = subRepo.findByCompanyId(UUID.fromString(companyId))

        val subsModSet: MutableSet<SubscriptionsModResponse> = mutableSetOf()

        for (sub in companySubs) {
            subsModSet.add(SubscriptionsModResponse(
                module = sub.module.toDTO(),
                subscription = sub.toDTO()
            ))
        }

        call.respond(HttpStatusCode.OK, subsModSet)
    }

    get("/possible-modules") {
        val session = authServ.authorizeOrThrow(
            call,
            requiredSuperAdmin = true
        )
        val possibleModules: Set<Module> = modRepo.getAll().toSet()
        val response: Set<ModuleDTO> = possibleModules.map { it.toDTO() }.toSet()
        call.respond(HttpStatusCode.OK, response)

    }

    post("/assign-module-to-company/{companyId}/{moduleId}") {
        val session = authServ.authorizeOrThrow(
            call,
            requiredSuperAdmin = true
        )
        val companyId = call.parameters["companyId"]
        val moduleId = call.parameters["moduleId"]
        if (companyId == null) {
            call.respond(HttpStatusCode.BadRequest, message = "NO_COMPANY_ID")
            return@post
        }
        if (moduleId == null) {
            call.respond(HttpStatusCode.BadRequest, message = "NO_MODULE_ID")
            return@post
        }
        val mod = modRepo.get(UUID.fromString(moduleId))
        if (mod == null) {
            call.respond(HttpStatusCode.BadRequest, message = "NO_MODULE")
            return@post
        }
        val company = companyRepo.findById(UUID.fromString(companyId))
        if (company == null) {
            call.respond(HttpStatusCode.BadRequest, message = "NO_COMPANY")
            return@post
        }

        val existingSub = subRepo.findByCompanyIdAndModule(UUID.fromString(companyId), mod.name)
        if (existingSub != null) {
            call.respond(HttpStatusCode.BadRequest, message = "SUBSCRIPTION_ALREADY_EXISTS")
            return@post
        }

        val sub = Subscription(
            company = company,
            module = mod,
            isActive = true,
            isAccessible = true
        )
        try {
            subRepo.create(sub)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, message = "ERROR_CREATING_SUBSCRIPTION")
            return@post
        }

        call.respond(HttpStatusCode.OK)

    }

    patch("/update-access-status/{subscriptionId}/{isAccessible}") {
        val session = authServ.authorizeOrThrow(
            call,
            requiredSuperAdmin = true
        )

        val subscriptionIdParam = call.parameters["subscriptionId"]
        val isAccessibleParam = call.parameters["isAccessible"]

        val subscriptionId = runCatching { UUID.fromString(subscriptionIdParam) }.getOrNull()
        val isAccessible = isAccessibleParam?.toBooleanStrictOrNull()

        if (subscriptionId == null) {
            call.respond(HttpStatusCode.BadRequest, message = "NO_SUBSCRIPTION_ID")
            return@patch
        }
        if (isAccessible == null) {
            call.respond(HttpStatusCode.BadRequest, message = "NO_IS_ACCESSIBLE")
            return@patch
        }

        val existentSubscription = subRepo.find(subscriptionId)
        if (existentSubscription == null) {
            call.respond(HttpStatusCode.BadRequest, message = "NO_SUBSCRIPTION")
            return@patch
        }

        try {
            subRepo.updateAccessStatus(isAccessible, subscriptionId)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, message = "ERROR_UPDATING_ACCESS_STATUS")
            return@patch
        }

        call.respond(HttpStatusCode.OK)

    }

}