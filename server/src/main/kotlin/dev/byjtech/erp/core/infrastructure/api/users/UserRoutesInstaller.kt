package dev.byjtech.erp.core.infrastructure.api.users

import dev.byjtech.erp.core.application.service.UserService
import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.domain.repository.SuperAdminRepository
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.jetbrains.exposed.sql.Database

class UserRoutesInstaller (
    private val userRepo: UserRepository,
    private val authServ: CoreAuthWrapper,
    private val moduleRepo: ModuleRepository,
    private val superAdminRepo: SuperAdminRepository
) : RoutesInstaller {
    override fun Route.installRoutes() {
        route("/users/tenant") {
            tenantUsers(authServ, userRepo, moduleRepo)
        }
        route("/users/super-admin") {
            superAdminUsers(authServ, userRepo)
        }
        route("/users"){
            get("me/type") {
                val session = authServ.authorizeOrThrow(call)
                val userId = userRepo.find(session.userId)

                if (userId == null) {
                    //nunca deveria pasar
                    call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                }else{
                    val userType = if (superAdminRepo.getByUserId(session.userId) != null) {
                        "superadmin"
                    } else {
                        "tenant"
                    }
                    call.respond(userType)
                }
            }
        }
    }
}