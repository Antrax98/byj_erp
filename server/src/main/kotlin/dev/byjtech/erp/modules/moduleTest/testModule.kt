package dev.byjtech.erp.modules.moduleTest

import dev.byjtech.erp.config.ModuleInitializer
import dev.byjtech.erp.modules.TestDefinition
import dev.byjtech.erp.modules.moduleTest.infrastructure.infrastructureModule
import dev.byjtech.erp.modules.moduleTest.infrastructure.TestTables
import dev.byjtech.erp.modules.moduleTest.infrastructure.api.TestRoutesInstaller
import dev.byjtech.erp.shared.infrastructure.database.CreateDatabase
import io.github.cdimascio.dotenv.dotenv
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dotenv = dotenv{
    ignoreIfMissing = false
}

val testModule = module {
    includes(
        infrastructureModule
    )

    single(named("testDatabase")){
        CreateDatabase(
            dotenv["TEST_DB_HOST"],
            dotenv["TEST_DB_PORT"],
            dotenv["TEST_DB_NAME"],
            dotenv["TEST_DB_USER"],
            dotenv["TEST_DB_PASSWORD"]
        )
    }

    single<ModuleInitializer>(named("testInit")) {
        TestInitializer(
            definition = TestDefinition,
            database = get(named("testDatabase")),
            tables = TestTables.all,
            moduleRoutesInstaller = get<TestRoutesInstaller>()
        )
    }
}