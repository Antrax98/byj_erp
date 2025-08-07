package dev.byjtech.erp.modules.machinery

import dev.byjtech.erp.config.ModuleInitializer
import dev.byjtech.erp.modules.machinery.application.machineryapplicationModule
import dev.byjtech.erp.modules.machinery.domain.machinerydomainModule
import dev.byjtech.erp.modules.machinery.infrastructure.api.MachineryRoutesInstaller
import dev.byjtech.erp.modules.machinery.infrastructure.machineryinfrastructureModule
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.MachineryTables
import dev.byjtech.erp.modules.machinery.MachineryDefinition
import dev.byjtech.erp.shared.infrastructure.database.CreateDatabase
import io.github.cdimascio.dotenv.dotenv
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dotenv = dotenv{
    ignoreIfMissing = false
}

val machineryModule = module {
    includes(
        machinerydomainModule,
        machineryinfrastructureModule,
        machineryapplicationModule,
    )

    single(named("machineryDatabase")){
        CreateDatabase(
            dotenv["MACHINERY_DB_HOST"],
            dotenv["MACHINERY_DB_PORT"],
            dotenv["MACHINERY_DB_NAME"],
            dotenv["MACHINERY_DB_USER"],
            dotenv["MACHINERY_DB_PASSWORD"])
    }

    single<ModuleInitializer>(named("machineryInit")) {
        MachineryInitializer(
            definition = MachineryDefinition,
            moduleRoutesInstaller = get<MachineryRoutesInstaller>(), //rutas
            database = get(named("machineryDatabase")),
            tables = MachineryTables.all

        )
    }
}
