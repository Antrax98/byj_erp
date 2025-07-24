package dev.byjtech.erp.core.infrastructure.api.companies

import dev.byjtech.erp.core.domain.repository.CompanyRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.domain.model.Company
import dev.byjtech.erp.core.domain.model.Subscription
import dev.byjtech.erp.core.domain.model.User
import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.domain.repository.RoleRepository
import dev.byjtech.erp.core.domain.repository.SubscriptionRepository
import dev.byjtech.erp.core.domain.repository.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toDTO
import dev.byjtech.erp.core.request.CreateCompanyRequest
import dev.byjtech.erp.core.response.ErrorList
import io.ktor.server.request.receive
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

fun Route.superAdminCompanies(authServ: CoreAuthWrapper, companyRepo: CompanyRepository, userRepo: UserRepository, moduleRepo: ModuleRepository, subscriptionRepo: SubscriptionRepository) {
    get("/all-companies"){
        authServ.authorizeOrThrow(call, requiredSuperAdmin = true)

        val companies = companyRepo.findAll().map { it.toDTO() }.toSet()

        call.respond(HttpStatusCode.OK, companies)

    }

    post("/create-company"){
        val authResult = authServ.authorizeOrThrow(call, requiredSuperAdmin = true)

        //esto esta aqui para aserciorarse que no se elimine este permiso y exista el Modulo Core
        val adminAllKey = CoreDefinition.Admin.All.key

        //TODO(): validar que no exista una empresa con el mismo rut y que el adminEmail no exista en otro usuario
        val createCompanyRequest = call.receive<CreateCompanyRequest>()

        //TODO: verificar que el adminEmail no exista en otro usuario
        val existCompany: Boolean = companyRepo.existWithRut(createCompanyRequest.companyRut)

        //TODO: verificar que el rut no exista en otra empresa
        val existUser: Boolean = userRepo.findByEmail(createCompanyRequest.adminEmail) != null

        //si uno o los dos ya existen enviar error con codigos
        if (existCompany || existUser) {
            call.respond(
                HttpStatusCode.Conflict,
                ErrorList(
                    buildList {
                        if (existCompany) add("COMPANY")
                        if (existUser) add("USER")
                    }
                )
            )
            return@post  //<- esto devuelve el call de inmediato
        }


        //crear company
        val newCompany = companyRepo.save(
            Company(
                name = createCompanyRequest.companyName,
                contactEmail = createCompanyRequest.companyContactEmail,
                rut = createCompanyRequest.companyRut,
            )
        )

        //añadir suscripcion a Core
        val coreModule = moduleRepo.findByName("core")!!
        val coreSubscription = subscriptionRepo.create(Subscription(
            company = newCompany,
            module = coreModule,
        ))

        //crear user con Admin role
        val newUser = userRepo.create(User(
            companyId = newCompany.id,
            name = createCompanyRequest.adminName,
            email = createCompanyRequest.adminEmail
        ))

        //el permiso all deve si o si existir
        val adminPermission = moduleRepo.findPermissionByPermissionKey(CoreDefinition.Admin.All.key)!!
        userRepo.addSpecialPermission(newUser.id, adminPermission.id)

        call.respond(HttpStatusCode.OK)
    }

    patch("/update-company-name/{companyId}/{name}"){
        authServ.authorizeOrThrow(call, requiredSuperAdmin = true)
        val companyId = call.parameters["companyId"]
        if (companyId == null) {
            call.respond(HttpStatusCode.BadRequest, "NO_COMPANY_ID")
            return@patch
        }
        val newName = call.parameters["name"]
        if (newName == null) {
            call.respond(HttpStatusCode.BadRequest, "NO_NEW_NAME")
            return@patch
        }
        val company = companyRepo.findById(UUID.fromString(companyId))
        if (company == null) {
            call.respond(HttpStatusCode.NotFound, "COMPANY_NOT_FOUND")
            return@patch
        }

        companyRepo.updateName(company.id, newName)
        call.respond(HttpStatusCode.OK)

    }

    patch("/update-company-contact-email/{companyId}/{email}") {
        authServ.authorizeOrThrow(call, requiredSuperAdmin = true)
        val companyId = call.parameters["companyId"]
        if (companyId == null) {
            call.respond(HttpStatusCode.BadRequest, "NO_COMPANY_ID")
            return@patch
        }
        val newEmail = call.parameters["email"]
        if (newEmail == null) {
            call.respond(HttpStatusCode.BadRequest, "NO_NEW_EMAIL")
            return@patch
        }
        val company = companyRepo.findById(UUID.fromString(companyId))
        if (company == null) {
            call.respond(HttpStatusCode.NotFound, "COMPANY_NOT_FOUND")
            return@patch
        }

        companyRepo.updateContactEmail(company.id, newEmail)
        call.respond(HttpStatusCode.OK)

    }


}