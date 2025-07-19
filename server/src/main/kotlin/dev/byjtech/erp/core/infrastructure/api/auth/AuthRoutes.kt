package dev.byjtech.erp.core.infrastructure.api.auth

import com.github.benmanes.caffeine.cache.Cache
import dev.byjtech.erp.config.fetchGoogleUserInfo
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.application.service.UserService
//import dev.byjtech.erp.core.database.superAdmins.SuperAdminDataSource.Companion.isSuperAdmin
//import dev.byjtech.erp.core.database.userSessions.UserSessionsDataSource.Companion.findUserIdBySessionId
//import dev.byjtech.erp.core.database.userSessions.UserSessionsDataSource as USDS
//import dev.byjtech.erp.core.database.users.findUserByEmail
//import dev.byjtech.erp.core.database.users.updateUserFromGoogleInfo
import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.domain.model.Session
import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.domain.repository.RoleRepository
import dev.byjtech.erp.core.domain.repository.SessionRepository
import dev.byjtech.erp.core.domain.repository.SubscriptionRepository
import dev.byjtech.erp.core.domain.repository.SuperAdminRepository
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toDTO
import dev.byjtech.erp.core.response.AccessibleModulesResponse
import dev.byjtech.erp.core.response.PermissionKeysResponse
import dev.byjtech.erp.core.response.PermittedModulesResponse
import dev.byjtech.erp.core.response.SubscribedModulesResponse
import dev.byjtech.erp.core.response.UserPermissionsResponse
import dev.byjtech.erp.core.response.UserRolesResponse
import dev.byjtech.erp.core.session.AppSession
import dev.byjtech.erp.utils.datetime.toKotlinx
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
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Base64
import java.util.UUID

val logger: Logger = LoggerFactory.getLogger("AuthCallbackLogger")




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
    // ANAYS ajuste este endpoint permite generar tokens Bearer válidos para testing en Postman
    get("/generate-admin-token") {
        try {
            // Buscar el usuario admin de prueba
            val adminUser = userRepo.findByEmail("usuariotesttesttester@gmail.com")
            if (adminUser == null) {
                call.respond(HttpStatusCode.NotFound, "Admin user not found")
                return@get
            }

            // Limpiar sesiones previas del postman para evitar duplicados
            // Esto asegura que solo haya una sesión activa por dispositivo
            try {
                // Buscar y eliminar sesiones existentes con el mismo device_id
                val existingSessions = sessionRepo.findByUserId(adminUser.id).filter { it.deviceId == "postman-test-v2" }
                existingSessions.forEach { sessionRepo.delete(it.id) }
            } catch (e: Exception) {
                logger.warn("No se pudieron limpiar las sesiones previas: ${e.message}")
            }

            // Crear una nueva sesión para el admin
            // IMPORTANTE: Este UUID se usa tanto para la BD como para el token
            val newSessionId = UUID.randomUUID()
            val expiresAt = Instant.now().plusSeconds(7 * 24 * 60 * 60) // 7 días
            
            // Crear el objeto Session con el mismo ID que se usará en el token
            val session = Session(
                id = newSessionId,
                userId = adminUser.id,
                deviceId = "postman-test-v2",
                accessTokens = "test-token",
                refreshToken = "test-refresh",
                platform = "postman",
                userAgent = "Postman Test Agent",
                isValid = true,
                createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                expiresAt = java.time.LocalDateTime.ofInstant(expiresAt, ZoneId.systemDefault()).toKotlinx()
            )
            
            // Guardar sesión en BD - ahora usa el ID correcto gracias al fix en SessionRepositoryImpl
            sessionRepo.create(session)
            
            // Crear el AppSession token con el MISMO sessionId que se guardó en BD
            val appSession = AppSession(newSessionId.toString(), expiresAt.toEpochMilli())
            val token = appSession.toEncoded()
            
            call.respond(mapOf(
                "token" to token,
                "sessionId" to newSessionId.toString(),
                "userId" to adminUser.id.toString(),
                "userEmail" to adminUser.email,
                "expiresAt" to expiresAt.toString(),
                "note" to "Use this token in Authorization header as: Authentication: Bearer $token"
            ))
            
        } catch (e: Exception) {
            logger.error("Error generating admin token", e)
            call.respond(HttpStatusCode.InternalServerError, "Error generating token: ${e.message}")
        }
    }

    //todo lo que ver con roles y user sacarlo de aqui a su propios rutes
    get("/userRoles/{userId}"){
        auth.authorizeOrThrow(call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Users.View.key,
                CoreDefinition.Admin.All.key
            )
        )
        val userId = call.parameters["userId"]?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
        val user = userRepo.find(UUID.fromString(userId))
        if (user == null) {
            call.respond(HttpStatusCode.NotFound, null)
            return@get
        } else {
            val userRoles = roleRepo.findByUserId(UUID.fromString(userId))
            val rolesDTO = userRoles.map { it.toDTO() }
            call.respond(UserRolesResponse(roles = rolesDTO))
        }

    }

    get("/userSpecialPermissions/{userId}"){
        auth.authorizeOrThrow(call,
            requiredAnyPermissions = setOf(
                CoreDefinition.Users.View.key,
                CoreDefinition.Admin.All.key
            )
        )
        val userId = call.parameters["userId"]?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
        val user = userRepo.find(UUID.fromString(userId))
        if (user == null) {
            call.respond(HttpStatusCode.NotFound, "User not found")
        } else {
            val userSpecialPermissions = userRepo.getSpecialPermissionsByUserId(UUID.fromString(userId))
            val nonnullUserSpecialPermissions = userSpecialPermissions?.map {it.id}?.toSet()?: emptySet()
            val listofPermissionKeys = moduleRepo.getPermissionKeysByPermissionIdSet(nonnullUserSpecialPermissions)
            call.respond(PermissionKeysResponse(listofPermissionKeys.toList()))
        }

    }

    //todo asta aqui
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

        val userType = if (superAdminRepo.getByUserId(session.userId)!=null) {
            "superadmin"
        } else {
            "tenant"
        }
        call.respond(userType)//TODO: enviar respuesta como un DTO???
    }

    get("/me") {
        val session = auth.authorizeOrThrow(call)
        val userId = session.userId
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

    // Ruta manual para iniciar el proceso de OAuth con Google
    get("/login") {
        try {
            // Usar la misma instancia de dotenv configurada en AuthConfig
            val dotenv = dev.byjtech.erp.config.dotenv
            
            // Obtener parámetros de la query string o crear valores por defecto
            val platform = call.request.queryParameters["platform"] ?: "desktop"
            val deviceId = call.request.queryParameters["deviceId"] ?: UUID.randomUUID().toString()
            
            // Crear el state JSON
            val stateMap = mapOf(
                "platform" to platform,
                "deviceId" to deviceId
            )
            val stateJson = Json.encodeToString(stateMap)
            val state = Base64.getUrlEncoder().encodeToString(stateJson.toByteArray())
            
            val clientId = dotenv.get("GOOGLE_CLIENT_ID") ?: throw IllegalStateException("GOOGLE_CLIENT_ID not configured")
            val redirectUri = dotenv.get("OAUTH_REDIRECT_URI") ?: throw IllegalStateException("OAUTH_REDIRECT_URI not configured")
            
            // Definir los scopes requeridos
            val scopes = listOf(
                "openid",
                "https://www.googleapis.com/auth/userinfo.email",
                "https://www.googleapis.com/auth/userinfo.profile"
            ).joinToString("+")
            
            // Construir la URL de autorización de Google
            val authUrl = "https://accounts.google.com/o/oauth2/auth" +
                "?client_id=$clientId" +
                "&redirect_uri=$redirectUri" +
                "&scope=$scopes" +
                "&response_type=code" +
                "&access_type=offline" +
                "&prompt=select_account" +
                "&state=$state"
            
            logger.debug("Redirecting to Google OAuth: $authUrl")
            
            // Redirigir al usuario a la URL de autorización de Google
            call.respondRedirect(authUrl)
            
        } catch (e: Exception) {
            logger.error("Error in /auth/login", e)
            call.respond(HttpStatusCode.InternalServerError, "Authentication error: ${e.message}")
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
            // Use the state parameter directly from Google OAuth callback
            val stateEncoded = call.request.queryParameters["state"] ?: ""
            logger.debug("stateEncoded: {}", stateEncoded)
            val redirect = call.request.queryParameters["redirect"]
            logger.debug("Entering /callback. state encoded: {}, redirect: {}", stateEncoded, redirect)
            if (principal != null && stateEncoded.isNotBlank()) {

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
                    val user = userRepo.findByEmail(googleUserInfo.email)
                    if (user != null) {
                        println("User found: ${user.id}")
                        println("Updating user from Google info...")
                        println(googleUserInfo)
                        userServ.updateUserFromGoogleInfo(user, googleUserInfo)

                        val userAgent = call.request.headers["User-Agent"] ?: ""
                        val sessionExpiresAt = if (googleUserInfo.expiresAt > 0) {
                            Instant.ofEpochMilli(googleUserInfo.expiresAt)
                        } else {
                            logger.warn("No expiration found in ID token. Using default (7 days).")
                            LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.UTC) //en 7 dias
                        }
                        val userSessionsSet = sessionRepo.findByUserId(user.id)
                        var existingSessionAux = userSessionsSet.firstOrNull {
                            it.deviceId == deviceId
                        }

                        val sessionCookie : AppSession
                        if (existingSessionAux != null && existingSessionAux.isValid) {
                            existingSessionAux = existingSessionAux.copy(
                                accessTokens = accessToken,
                                refreshToken = principal.refreshToken ?: "",
                                userAgent = userAgent,
                                platform = platform,
                                expiresAt = LocalDateTime.ofInstant(sessionExpiresAt, ZoneId.systemDefault()).toKotlinx() // Update expiration
                            )
                            sessionRepo.update(existingSessionAux)
                            sessionCookie = AppSession(existingSessionAux.id.toString(), sessionExpiresAt.toEpochMilli())
                            logger.debug("Updated appSession: {}", sessionCookie)
                        } else {
//                            val newSession = USDS.createSession( //TODO MODIFICAR
//                                userId = userEntity.id.value,
//                                accessToken = accessToken,
//                                refreshToken = principal.refreshToken ?: "",
//                                userAgent = userAgent,
//                                deviceId = deviceId,
//                                platform = platform,
//                                expiresAt = sessionExpiresAt  // Store expiration
//                            )
                            val newSession = sessionRepo.create(Session(
                                userId = user.id,
                                id = UUID.randomUUID(), //este no importa pero hay que darlo igual
                                deviceId = deviceId,
                                accessTokens = accessToken,
                                refreshToken = principal.refreshToken ?: "",
                                platform = platform,
                                userAgent = userAgent,
                                isValid = true,
                                createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                                updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                                expiresAt = LocalDateTime.ofInstant(sessionExpiresAt, ZoneId.systemDefault()).toKotlinx(),
                            ))
                            sessionCookie = AppSession(newSession.id.toString(), sessionExpiresAt.toEpochMilli())
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

    // ENDPOINT DUPLICADO - ELIMINAR
    // Este endpoint está duplicado arriba con mejor implementación
    // TODO: Eliminar todo este bloque de código duplicado
    /*
    get("/generate-admin-token") {
        try {
            // Buscar el usuario admin de prueba
            val adminUser = userRepo.findByEmail("usuariotesttesttester@gmail.com")
            if (adminUser == null) {
                call.respond(HttpStatusCode.NotFound, "Admin user not found")
                return@get
            }

            // Crear una nueva sesión para el admin
            val newSessionId = UUID.randomUUID()
            val expiresAt = Instant.now().plusSeconds(7 * 24 * 60 * 60) // 7 días
            
            val session = Session(
                id = newSessionId,
                userId = adminUser.id,
                deviceId = "postman-test",
                accessTokens = "test-token",
                refreshToken = "test-refresh",
                platform = "postman",
                userAgent = "Postman Test Agent",
                isValid = true,
                createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                expiresAt = LocalDateTime.ofInstant(expiresAt, ZoneId.systemDefault()).toKotlinx()
            )
            
            sessionRepo.create(session)
            
            // Crear el AppSession token
            val appSession = AppSession(newSessionId.toString(), expiresAt.toEpochMilli())
            val token = appSession.toEncoded()
            
            call.respond(mapOf(
                "token" to token,
                "sessionId" to newSessionId.toString(),
                "userId" to adminUser.id.toString(),
                "userEmail" to adminUser.email,
                "expiresAt" to expiresAt.toString(),
                "note" to "Use this token in Authorization header as: Authentication: Bearer $token"
            ))
            
        } catch (e: Exception) {
            logger.error("Error generating admin token", e)
            call.respond(HttpStatusCode.InternalServerError, "Error generating token: ${e.message}")
        }
    }
    */

}