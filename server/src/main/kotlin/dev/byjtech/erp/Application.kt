package dev.byjtech.erp

import dev.byjtech.erp.config.configureOAuth
import dev.byjtech.erp.config.database.DatabaseFactory
import dev.byjtech.erp.config.database.DatabaseInitializer
import dev.byjtech.erp.core.routes.googleAuthRoutes
import io.ktor.client.HttpClient
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.client.engine.cio.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*
import io.ktor.http.HttpStatusCode
import dev.byjtech.erp.core.routes.logger
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.Cache
import dev.byjtech.erp.core.Core
import dev.byjtech.erp.core.auth.authenticateAndAuthorize
import dev.byjtech.erp.core.database.CoreTables
import dev.byjtech.erp.core.database.userSessions.UserSessionsDataSource
import dev.byjtech.erp.core.routes.CoreRoutes
import java.util.concurrent.TimeUnit
import dev.byjtech.erp.modules.announcements.v1.AnnouncementsV1
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.routing.get

val dotenv = dotenv{
    ignoreIfMissing = false
}

fun main() {
    embeddedServer(Netty, port = dotenv["SERVER_PORT"].toInt(), host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    //inicializar base de datos
    val databaseFactory = DatabaseFactory()
    val database = databaseFactory.database

    //inicializar las tablas (por si es que no existen)
    val databaseInitializer = DatabaseInitializer(database)
//databaseInitializer.nuke() //borra las tablas
    try {
        databaseInitializer.initialize(
            *Core.tables.toTypedArray()+
            AnnouncementsV1.tables.toTypedArray()
        )
    } catch (e: Exception) {
        println("Las tablas ya existen o hubo un problema: ${e.message}")
    }


    //TODO() mandarlo a su propia funcion para que no moleste con los imports
    val httpClient = HttpClient(CIO) {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        })
    }

    val stateCache: Cache<String, String> = Caffeine.newBuilder()
        .expireAfterWrite(20, TimeUnit.MINUTES) // Tiempo de vida del state
        .maximumSize(10_000)                   // Tamaño máximo del cache
        .build()


    configureOAuth(httpClient, stateCache)





    install(RoutingRoot){
        route("/"){
            get {
                call.respondText { "Ktor: ${Greeting().greet()}" }
            }
        }

        route("/auth"){
            googleAuthRoutes(httpClient, stateCache)
        }



        route("/api"){
            //aqui agregar las rutas autenticadas para los modulos
            Core.installRoutes(this)
            AnnouncementsV1.installRoutes(this)

        }

        route("/testAdmin"){
            get {
                //logger.debug("SESSION of SuperAdmin : {}", call.attributes[sessionKey])
                val session =authenticateAndAuthorize(call, requiredAdmin = true)
                logger.debug("SESSION of SuperAdmin : {}", session)
                logger.debug("si esto se imprime y no se tiene superAdmin, significa que hay que hacer cambios en la verificacion de permisos")
                call.respondText("Hello Admin")
            }
        }

    }
}