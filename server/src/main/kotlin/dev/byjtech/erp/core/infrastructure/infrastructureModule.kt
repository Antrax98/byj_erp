package dev.byjtech.erp.core.infrastructure

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import dev.byjtech.erp.shared.contracts.core.auth.AuthServiceContract
import dev.byjtech.erp.core.domain.repository.*
import dev.byjtech.erp.core.infrastructure.api.CoreRoutesInstaller
import dev.byjtech.erp.core.infrastructure.exposed.repository.*
import dev.byjtech.erp.core.infrastructure.api.auth.AuthRoutesInstaller
import dev.byjtech.erp.core.infrastructure.api.users.UserRoutesInstaller
import org.koin.dsl.module

//contracts
import dev.byjtech.erp.core.infrastructure.auth.AuthServiceContractImpl
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.core.infrastructure.exposed.CoreTables
import dev.byjtech.erp.shared.routing.ModuleRoutesInstaller
import dev.byjtech.erp.shared.routing.RoutesInstaller

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Table
import java.util.concurrent.TimeUnit



val httpClient = HttpClient(CIO) {
    install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}
val stateCache: Cache<String, String> = Caffeine.newBuilder()
    .expireAfterWrite(20, TimeUnit.MINUTES) // Tiempo de vida del state
    .maximumSize(10_000)                   // Tamaño máximo del cache
    .build()

//TODO: aqui va todo lo que se refiera a conecciones con el exterior
// los repositoryImpl por que  hablan con una base de datos (se considera externo)
// el authServiceContract por ser un contract externo
// coreAuthWrapper por ser un wrapper externo
// los instaladores de rutas (y por consiguiente sus rutas) por ser los que hablan al exterior (por lo que entiendo, hacerlo a tu propia discrecion)
val infrastructureModule = module {
    //repositories
    single<CompanyRepository> { CompanyRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }
    single<SessionRepository> { SessionRepositoryImpl() }
    single<SuperAdminRepository> { SuperAdminRepositoryImpl() }
    single<RoleRepository> { RoleRepositoryImpl() }
    single<ModuleRepository> { ModuleRepositoryImpl() }
    single<SubscriptionRepository> { SubscriptionRepositoryImpl() }

    //tables
    //single<CoreTables> { CoreTables } //no supe como hacerlo funcionar, lo dare directametne en el initializer por ahora

    //contracts
    single<AuthServiceContract> { AuthServiceContractImpl(get(), get(), get(), get(), get()) }

    //authWraper
    single<CoreAuthWrapper> { CoreAuthWrapper(get()) }

    //instancias necesarias de auth
    single<HttpClient> { httpClient }
    single<Cache<String, String>> { stateCache }

    //route installers
    single<AuthRoutesInstaller> {
        AuthRoutesInstaller(
            httpClient = get(),
            stateCache = get(),
            userRepo = get(),
            userServ = get(),
            sessionRepo = get(),
            superAdminRepo = get(),
            moduleRepo = get(),
            auth = get()
        )
    }

    single<UserRoutesInstaller> {
        UserRoutesInstaller(
            userServ = get()
        )
    }

    //routing
    single<CoreRoutesInstaller> {
        CoreRoutesInstaller(
            setOf(
                //AuthRoutesInstaller(), //TODO: justamente separar esta de las demas rutas, los otros si van aqui
                UserRoutesInstaller(get())
            )
        )
    }
}