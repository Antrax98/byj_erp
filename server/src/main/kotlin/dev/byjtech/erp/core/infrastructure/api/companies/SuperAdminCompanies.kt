package dev.byjtech.erp.core.infrastructure.api.companies

import dev.byjtech.erp.core.domain.repository.CompanyRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.domain.model.Company
import dev.byjtech.erp.core.domain.model.User
import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.domain.repository.RoleRepository
import dev.byjtech.erp.core.domain.repository.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toDTO
import dev.byjtech.erp.core.request.CreateCompanyRequest
import io.ktor.server.request.receive
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

fun Route.superAdminCompanies(authServ: CoreAuthWrapper, companyRepo: CompanyRepository, userRepo: UserRepository, moduleRepo: ModuleRepository) {
    get("/all-companies"){
        authServ.authorizeOrThrow(call, requiredSuperAdmin = true)

        val companies = companyRepo.findAll().map { it.toDTO() }.toSet()

        call.respond(HttpStatusCode.OK, companies)

    }

    post("/create-company"){
        val authResult = authServ.authorizeOrThrow(call, requiredSuperAdmin = true)

        //TODO(): validar que no exista una empresa con el mismo rut y que el adminEmail no exista en otro usuario
        val createCompanyRequest = call.receive<CreateCompanyRequest>()
        //crear company
        val newCompany = companyRepo.save(
            Company(
                id = UUID.randomUUID(),
                name = createCompanyRequest.name,
                contactEmail = createCompanyRequest.contactEmail,
                createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                rut = createCompanyRequest.rut
            )
        )
        //crear user con Admin role
        val newUser = userRepo.create(User(
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            googleId = null,
            pictureUrl = null,
            isActive = true,
            companyId = newCompany.id,
            roles = null,
            specialPermissions = null,
            id = UUID.randomUUID(),
            name = null,
            email = createCompanyRequest.adminEmail
        ))

        val adminPermission = moduleRepo.findPermissionByPermissionKey(CoreDefinition.Admin.All.key)
        if(adminPermission != null) {
            userRepo.addSpecialPermission(newUser.id, adminPermission.id)
        }



        call.respond(HttpStatusCode.OK)
    }

}