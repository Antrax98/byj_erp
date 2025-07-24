package dev.byjtech.erp.core

import dev.byjtech.erp.config.ModuleInitializer
import dev.byjtech.erp.core.domain.domainModule
import dev.byjtech.erp.core.infrastructure.infrastructureModule
import dev.byjtech.erp.core.application.applicationModule
import dev.byjtech.erp.core.infrastructure.api.CoreRoutesInstaller
import dev.byjtech.erp.core.infrastructure.api.auth.AuthRoutesInstaller
import dev.byjtech.erp.core.infrastructure.exposed.CoreTables
import dev.byjtech.erp.shared.infrastructure.database.CreateDatabase
//import dev.byjtech.erp.shared.infrastructure.database.DatabaseFactory
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database
import org.koin.core.qualifier.named
import org.koin.dsl.module



val coreModule = module {
    includes(
        domainModule,
        applicationModule,
        infrastructureModule
    )

    val dotenv = dotenv{
        ignoreIfMissing = true
    }


    single(named("coreDatabase")) {
        CreateDatabase(
            System.getenv("DB_HOST") ?: dotenv.get("DB_HOST") ?: error("DB_HOST not set"),
            System.getenv("DB_PORT") ?: dotenv.get("DB_PORT") ?: error("DB_PORT not set"),
            System.getenv("DB_NAME") ?: dotenv.get("DB_NAME") ?: error("DB_NAME not set"),
            System.getenv("DB_USER") ?: dotenv.get("DB_USER") ?: error("DB_USER not set"),
            System.getenv("DB_PASSWORD") ?: dotenv.get("DB_PASSWORD") ?: error("DB_PASSWORD not set")
        )
    }




    //TODO() posiblemente hacer lo mismo de routes pero con los permission y las tablas
    single<ModuleInitializer>(named("coreInit")) {
        CoreInitializer(
            definition = CoreDefinition, //son el nombre y los permisos con category
            moduleRoutesInstaller = get<CoreRoutesInstaller>(), //rutas
            authRoutesInstaller = get<AuthRoutesInstaller>(),
            database = get(named("coreDatabase")),
            tables = CoreTables.all //esto debio ser mas elegante pero son las 5 de la mañana //Tablas
            //igual se puede guardar en un single separado con un interface para todas las tablas,
            // pero no sabria como asegurarme del orden (puede que se inicialisen las tablas de un modulo antes que las del core)
        )
    }
}