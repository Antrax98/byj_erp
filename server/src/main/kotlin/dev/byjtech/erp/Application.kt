package dev.byjtech.erp

import dev.byjtech.erp.config.configureOAuth
import io.ktor.client.HttpClient
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*
import com.github.benmanes.caffeine.cache.Cache
import dev.byjtech.erp.config.ModuleInitializer
import dev.byjtech.erp.shared.contracts.core.auth.AuthenticationException
import dev.byjtech.erp.shared.contracts.core.auth.AuthorizationException
import dev.byjtech.erp.core.CoreInitializer
import dev.byjtech.erp.shared.infrastructure.database.DatabaseInitializer
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.routing.get
import org.koin.ktor.ext.getKoin
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.LoggerFactory

val dotenv = dotenv{
    ignoreIfMissing = false
}

fun main() {
    embeddedServer(Netty, port = dotenv["SERVER_PORT"].toInt(), host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

private val logger = LoggerFactory.getLogger("Server Main")

fun Application.module() {



    //intalacion de Koin y sus modulos en set
    install(Koin){
        slf4jLogger()
        modules(
            serverModule
        )
    }

    //val koin = GlobalContext.get()
    val koin = this.getKoin()
    val moduleInitializers: List<ModuleInitializer> = koin.getAll()
    val allModules = moduleInitializers.filterNot { it is CoreInitializer } //sin el core, de ser necesario
    val coreInitializer = moduleInitializers.find { it is CoreInitializer } as CoreInitializer

    moduleInitializers.forEach {
        println("Module ${it.definition.name} detected")
    }


    val databaseInitializer: DatabaseInitializer = koin.get()

    println("Initializing database...")
    databaseInitializer.multiCreate(moduleInitializers)
    databaseInitializer.registerModuleDefinitions(moduleInitializers)
    println("Database initialized")
    println("Initializing first data...")
    databaseInitializer.firstDataInitialization()
    println("First data initialized")

    //databaseInitializer.nuke() //borra las tablas.. no sirbe para nada, hacerlo manualmente mejor por ahora

    install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        })
    }

    val httpClient: HttpClient = koin.get<HttpClient>()
    val stateCache: Cache<String, String> = koin.get<Cache<String, String>>()

    configureOAuth(httpClient, stateCache)


    configureStatusPages()




    install(RoutingRoot){
        route("/"){
            get {
                call.respondText { "Ktor: ${Greeting().greet()}" }
            }

            //las instalara en /auth, son solo las de autenticacion
            with(coreInitializer){
                this@route.installAuthRoutes()
            }
        }

        route("/api") {
            moduleInitializers.forEach { initializer ->
                with(initializer) {
                    this@route.installRoutes()
                }
            }
        }

    }.run {
        val allMyBase = getAllRoutes()
        logger.debug("--- All Routes:\n${allMyBase.joinToString("\n")}")
    }
}


fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<AuthenticationException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, mapOf("error" to cause.message))
        }
        exception<AuthorizationException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden, mapOf("error" to cause.message))
        }
        exception<Throwable> { call, cause ->
            call.application.environment.log.error("Unhandled exception", cause)
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Internal server error"))
        }
    }
}
