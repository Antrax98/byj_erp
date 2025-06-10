package dev.byjtech.erp.core.infrastructure.api.users

import dev.byjtech.erp.core.application.service.UserService
import dev.byjtech.erp.core.auth.authenticateAndAuthorize
import dev.byjtech.erp.core.database.superAdmins.SuperAdminDataSource.Companion.isSuperAdmin
import dev.byjtech.erp.core.database.userSessions.UserSessionsDataSource.Companion.findUserIdBySessionId
import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

class UserRoutesInstaller (
    private val userRepo: UserRepository,
    private val authServ: CoreAuthWrapper,
    private val moduleRepo: ModuleRepository
) : RoutesInstaller {
    override fun Route.installRoutes() {
        route("/users/tenant") {
            tenantUsers(authServ, userRepo, moduleRepo)
        }
        route("/users/superadmin") {
            superAdminUsers(authServ)
        }
        route("/users"){
            get("me/type") {
                val session = authenticateAndAuthorize(call)
                val userId = findUserIdBySessionId(session.id.value)

                if (userId == null) {
                    //nunca deveria pasar
                    call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                }else{
                    val userType = if (isSuperAdmin(userId)) {
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