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

val dotenv = dotenv{
    ignoreIfMissing = false
}

val coreModule = module {
    includes(
        domainModule,
        applicationModule,
        infrastructureModule
    )

    single(named("coreDatabase")){
        CreateDatabase(dotenv["DB_HOST"],dotenv["DB_PORT"],dotenv["DB_NAME"],dotenv["DB_USER"],dotenv["DB_PASSWORD"])
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