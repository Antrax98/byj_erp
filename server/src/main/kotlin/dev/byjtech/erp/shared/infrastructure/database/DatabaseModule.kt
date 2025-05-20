package dev.byjtech.erp.shared.infrastructure.database

import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val databaseModule = module {
    single<Database> {DatabaseFactory().database} //porsiacaso
    single<DatabaseInitializer> {DatabaseInitializer(get())}
}