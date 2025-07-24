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



val testModule = module {

//    val dotenv = dotenv{
//        ignoreIfMissing = true
//    }

    includes(
        infrastructureModule
    )

//    single(named("testDatabase")) {
//        CreateDatabase(
//            System.getenv("TEST_DB_HOST") ?: dotenv.get("TEST_DB_HOST") ?: error("TEST_DB_HOST not set"),
//            System.getenv("TEST_DB_PORT") ?: dotenv.get("TEST_DB_PORT") ?: error("TEST_DB_PORT not set"),
//            System.getenv("TEST_DB_NAME") ?: dotenv.get("TEST_DB_NAME") ?: error("TEST_DB_NAME not set"),
//            System.getenv("TEST_DB_USER") ?: dotenv.get("TEST_DB_USER") ?: error("TEST_DB_USER not set"),
//            System.getenv("TEST_DB_PASSWORD") ?: dotenv.get("TEST_DB_PASSWORD") ?: error("TEST_DB_PASSWORD not set")
//        )
//    }


    single<ModuleInitializer>(named("testInit")) {
        TestInitializer(
            definition = TestDefinition,
            database = null,//get(named("testDatabase")),
            tables = TestTables.all,
            moduleRoutesInstaller = get<TestRoutesInstaller>()
        )
    }
}