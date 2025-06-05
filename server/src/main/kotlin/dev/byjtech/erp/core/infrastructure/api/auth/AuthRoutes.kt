package dev.byjtech.erp.core.infrastructure.api.auth

import com.github.benmanes.caffeine.cache.Cache
import dev.byjtech.erp.config.fetchGoogleUserInfo
import dev.byjtech.erp.core.application.service.UserService
import dev.byjtech.erp.core.auth.authenticateAndAuthorize
import dev.byjtech.erp.core.database.superAdmins.SuperAdminDataSource.Companion.isSuperAdmin
import dev.byjtech.erp.core.database.userSessions.UserSessionsDataSource.Companion.findUserIdBySessionId
import dev.byjtech.erp.core.database.userSessions.UserSessionsDataSource as USDS
import dev.byjtech.erp.core.database.users.findUserByEmail
import dev.byjtech.erp.core.database.users.updateUserFromGoogleInfo
import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.domain.repository.RoleRepository
import dev.byjtech.erp.core.domain.repository.SessionRepository
import dev.byjtech.erp.core.domain.repository.SubscriptionRepository
import dev.byjtech.erp.core.domain.repository.SuperAdminRepository
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toDTO
import dev.byjtech.erp.core.response.AccessibleModulesResponse
import dev.byjtech.erp.core.response.PermittedModulesResponse
import dev.byjtech.erp.core.response.SubscribedModulesResponse
import dev.byjtech.erp.core.response.UserPermissionsResponse
import dev.byjtech.erp.core.session.AppSession
import java.time.LocalDateTime
import io.ktor.client.HttpClient
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.toMap
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.Base64

val logger: Logger = LoggerFactory.getLogger("AuthCallbackLogger")

//data class UserPrincipal(
//    val userId: Int,
//    val name: String,
//    val email: String,
//    val picture: String?
//)

//TODO: actualizarlo completamente de forma que solo use services y talvez repositories, no entities directamente

fun Route.googleAuthRoutes(
    httpClient: HttpClient,
    stateCache: Cache<String, String>,
    userRepo: UserRepository,
    userServ: UserService,
    roleRepo: RoleRepository,
    subscriptionRepo: SubscriptionRepository,
    sessionRepo: SessionRepository,
    superAdminRepo: SuperAdminRepository, //los que no use se borran despues
    moduleRepo: ModuleRepository,
    auth: CoreAuthWrapper
) {
    get("/test") {
        val session = auth.authorizeOrThrow(call)
        println("Request Info:")
        println("Method: ${call.request.httpMethod}")
        println("URL: ${call.request.uri}")
        println("Headers: ${call.request.headers.toMap()}")
        println("Body: ${call.receiveText()}")
        call.respondText("Debugging complete.")
    }

    //los activos (Active) por el admin y el Superadmin (Accessible) al mismo timepo
    // No developerOnly
    get("/permitted-modules") {
        val session = auth.authorizeOrThrow(call)
        val user = userRepo.find(session.userId)
        if (user?.companyId == null) {
            call.respond(HttpStatusCode.NotFound, "User and/or Company not found")
        } else {
            val companySubscriptions = subscriptionRepo.findByCompanyId(user.companyId).filter { it.isActive && it.isAccessible }
            //aqui las subscripciones tienen tanto la compañia como los modulos (sin categorias y permisos)
            val companyModules = companySubscriptions.map { it.module.toDTO() }.filter { !it.developerOnly }.toSet()
            logger.debug("User {} permitted modules: {}", user.name, companyModules)
            call.respond(PermittedModulesResponse(companyModules))
        }

    }

    //los subscriptos por el tenant(compañia), tanto los activos como los inactivos, como los que el Superadmin haya dejado inaccesibles
    //cosa de saber todos los modulos subscritos, que no sean developer only
    // No DeveloperOnly
    get("/subscribed-modules") {
        val session = auth.authorizeOrThrow(call)
        val user = userRepo.find(session.userId)
        if (user?.companyId == null) {
            call.respond(HttpStatusCode.NotFound, "User and/or Company not found")
        } else {
            val companySubscriptions = subscriptionRepo.findByCompanyId(user.companyId)
            //aqui las subscripciones tienen tanto la compañia como los modulos (sin categorias y permisos) // todo: agregar billings?
            val companyModules = companySubscriptions.map { it.module.toDTO() }.filter { !it.developerOnly }.toSet()
            logger.debug("User {} subscribed modules: {}", user.name, companyModules)
            call.respond(SubscribedModulesResponse(companyModules))
        }

    }

    //los marcados accessible por el SuperAdmin, incluyendo los desativados por el admin(tenant)
    //no DeveloperOnly
    get("/accessible-modules") {
        val session = auth.authorizeOrThrow(call)
        val user = userRepo.find(session.userId)
        if (user?.companyId == null) {
            call.respond(HttpStatusCode.NotFound, "User and/or Company not found")
        } else {
            val companySubscriptions = subscriptionRepo.findByCompanyId(user.companyId).filter { it.isAccessible }
            //aqui las subscripciones tienen tanto la compañia como los modulos (sin categorias y permisos)
            val companyModules = companySubscriptions.map { it.module.toDTO() }.filter { !it.developerOnly }.toSet()
            logger.debug("User {} accessible modules: {}", user.name, companyModules)
            call.respond(AccessibleModulesResponse(companyModules))
        }
    }

    get("/user-permissions") {
        val session = auth.authorizeOrThrow(call)
        val user = userRepo.find(session.userId)
        if (user == null) {
            call.respond(HttpStatusCode.NotFound, "User not found") // no deveria de pasar
        } else {
            val userRoles = roleRepo.findByUserId(user.id)
            var userPermissions: Set<Permission> = userRoles
                .flatMap { role -> roleRepo.getPermissionsByRoleId(role.id) }
                .toSet()
            val userSpecialPermissions = userRepo.getSpecialPermissionsByUserId(user.id)?: emptySet()
            userPermissions = userPermissions + userSpecialPermissions
            val userPermissionKeys = moduleRepo.getPermissionKeysByPermissionIdSet(userPermissions.map { it.id }.toSet())
            logger.debug("User {} permissions: {}",user.name, userPermissionKeys)
            call.respond(UserPermissionsResponse(userPermissionKeys))
        }
    }

    get("/me/type") {
        val session = auth.authorizeOrThrow(call)

        val userType = if (isSuperAdmin(session.userId)) {
            "superadmin"
        } else {
            "tenant"
        }
        call.respond(userType)//TODO: enviar respuesta como un DTO???
    }

    get("/me") {
        val session = auth.authorizeOrThrow(call)
        val userId = findUserIdBySessionId(session.sessionId)?: return@get call.respond(HttpStatusCode.NotFound, "User not found")
        val user = userRepo.find(userId)
        if (user == null) {
            call.respond(HttpStatusCode.NotFound, "User not found")
        } else {
            call.respond(user.toDTO())
        }
    }

    route("/logout"){
        get {
            val session = auth.authorizeOrThrow(call)
            sessionRepo.delete(session.sessionId)
            logger.debug("User {} logged out session id: {}", userRepo.find(session.userId)?.name, session.sessionId)
            call.respondText("Logout", status = HttpStatusCode.OK)
        }

    }

    //TODO: quitar todos los logger.debug()
    authenticate("google-oauth") {
        get("/login") {
            val stateEncoded = call.request.queryParameters["state"]
            logger.debug("stateEncoded: {}", stateEncoded)
            val stateJson = String(Base64.getUrlDecoder().decode(stateEncoded))
            logger.debug("stateJson {}",stateJson)
            val stateMap = Json.decodeFromString<Map<String, String>>(stateJson)
            logger.debug("stateMap {}",stateMap)
            val platform = stateMap["platform"] ?: "unknown"
            val deviceId = stateMap["deviceId"] ?: "unknown"
            logger.debug("decoded platform: {}, deviceId: {}",platform,deviceId)
        }
        get("/callback") {
            logger.debug("Entering /callback")
            val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
            logger.info("principal = $principal")
            val stateEncoded = stateCache.getIfPresent(call.request.queryParameters["state"]?:"")
            stateCache.invalidate(call.request.queryParameters["state"]?:"")
            logger.debug("stateEncoded: {}", stateEncoded)
            val redirect = call.request.queryParameters["redirect"]
            logger.debug("Entering /callback. state encoded: {}, redirect: {}", stateEncoded, redirect)
            if (principal != null && stateEncoded != null) {

                // se decodifica el state enviado por el frontend
                val stateJson = String(Base64.getUrlDecoder().decode(stateEncoded))
                logger.debug("stateJson {}",stateJson)
                val stateMap = Json.decodeFromString<Map<String, String>>(stateJson)
                logger.debug("stateMap {}",stateMap)
                val platform = stateMap["platform"] ?: "unknown"
                val deviceId = stateMap["deviceId"] ?: "unknown"
                logger.debug("decoded platform: {}, deviceId: {}",platform,deviceId)
                val accessToken = principal.accessToken
                val idToken = principal.extraParameters["id_token"]
                val googleUserInfo = fetchGoogleUserInfo(httpClient, accessToken, idToken) //NOTE: posiblemente hacerlo un service


                if (googleUserInfo != null) {
                    val userEntity = findUserByEmail(googleUserInfo.email)  //TODO MODIFICAR
                    if (userEntity != null) {
                        updateUserFromGoogleInfo(userEntity, googleUserInfo)  //TODO MODIFICAR
                        val userAgent = call.request.headers["User-Agent"] ?: ""
                        val sessionExpiresAt = if (googleUserInfo.expiresAt > 0) {
                            Instant.ofEpochMilli(googleUserInfo.expiresAt)
                        } else {
                            logger.warn("No expiration found in ID token. Using default (7 days).")
                            LocalDateTime.now().plusDays(7).toInstant(java.time.ZoneOffset.UTC) //Example: 7 days from now. Adjust as needed
                        }
                        val existingSession = USDS.findSessionByUserIdAndDeviceId(userEntity.id.value, deviceId)  //TODO MODIFICAR
                        val sessionCookie : AppSession
                        if (existingSession != null && existingSession.isValid) {
                            // actualizar el ya existente
                            USDS.updateSession( //TODO MODIFICAR
                                existingSession.id.value,
                                accessToken = accessToken,
                                refreshToken = principal.refreshToken ?: "",
                                userAgent = userAgent,
                                platform = platform,
                                expiresAt = sessionExpiresAt // Update expiration
                            )
                            sessionCookie = AppSession(existingSession.id.value, sessionExpiresAt.toEpochMilli())
                            logger.debug("Updated appSession: {}", sessionCookie)
                        } else {
                            val newSession = USDS.createSession( //TODO MODIFICAR
                                userId = userEntity.id.value,
                                accessToken = accessToken,
                                refreshToken = principal.refreshToken ?: "",
                                userAgent = userAgent,
                                deviceId = deviceId,
                                platform = platform,
                                expiresAt = sessionExpiresAt  // Store expiration
                            )
                            sessionCookie = AppSession(newSession.id.value, sessionExpiresAt.toEpochMilli())
                            logger.debug("Created new appSession: {}", sessionCookie)
                        }
                        val jsonCookie = Json.encodeToString(AppSession.serializer(), sessionCookie)
                        val encodedCookie = Base64.getUrlEncoder().encodeToString(jsonCookie.toByteArray())
                        logger.debug("Redirecting from /callback to: {}, platform {}", "$redirect?appSession=$encodedCookie",platform)
                        // dependiendo de la plataforma, hay que usar un redirect diferente, aqui añadir el de IOS de implementarse
                        //TODO: se podria hacer que el app en si envie su callback, solo deveria guardarlo al momento de hacer el login (en el StateCache junto al state)
                        when (platform) {
                            "android" -> {
                                call.respondRedirect("erpapp://callback?appSession=$encodedCookie")
                            }
                            "desktop" -> {
                                call.respondRedirect("http://localhost:12345/callback?appSession=$encodedCookie")
                            }
                            "ios" -> {
                                TODO("IOS not implemented yet")
                            }
                            else -> {
                                // Redirección por defecto o manejo de error si no se envia l aplataforma
                                call.respondText("unknown platform",status = HttpStatusCode.BadRequest, contentType = ContentType.Text.Plain)
                            }
                        }
                    } else {
                        call.respondText("User not found. Access Denied.", status = HttpStatusCode.Unauthorized, contentType = ContentType.Text.Plain)
                    }
                } else {
                    call.respondText("Failed to fetch user information from Google.", status = HttpStatusCode.InternalServerError, contentType = ContentType.Text.Plain)
                }
            } else {
                logger.error("principal or stateEncoded is null")
                call.respondText("Authentication failed.", status = HttpStatusCode.Unauthorized, contentType = ContentType.Text.Plain)
            }
        }
    }

}