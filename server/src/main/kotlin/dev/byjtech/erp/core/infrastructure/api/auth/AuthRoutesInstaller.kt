package dev.byjtech.erp.core.infrastructure.api.auth

import com.github.benmanes.caffeine.cache.Cache
import dev.byjtech.erp.core.application.service.UserService
import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.domain.repository.RoleRepository
import dev.byjtech.erp.core.domain.repository.SessionRepository
import dev.byjtech.erp.core.domain.repository.SubscriptionRepository
import dev.byjtech.erp.core.domain.repository.SuperAdminRepository
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.server.routing.Route
import io.ktor.client.HttpClient
import io.ktor.server.routing.route
import org.jetbrains.exposed.sql.Database

//TODO() ESTE ES ESPECIAL Y TIENE QUE INSTALAR LAS RUTAS DIRECTAMENTE EN LA RAIZ DEL SERVIDOR o en /auth
// NO USAR ESTE COMO REFERENCIA

class AuthRoutesInstaller(
    private val httpClient: HttpClient,
    private val stateCache: Cache<String, String>,
    private val userRepo: UserRepository,
    private val userServ: UserService,
    private val roleRepo: RoleRepository,
    private val subscriptionRepo: SubscriptionRepository,
    private val sessionRepo: SessionRepository,
    private val superAdminRepo: SuperAdminRepository,
    private val moduleRepo: ModuleRepository,
    private val auth: CoreAuthWrapper
): RoutesInstaller {
    override fun Route.installRoutes() {
        route("/auth"){
            googleAuthRoutes(httpClient, stateCache, userRepo, userServ, roleRepo, subscriptionRepo, sessionRepo, superAdminRepo, moduleRepo, auth)
        }
    }

}