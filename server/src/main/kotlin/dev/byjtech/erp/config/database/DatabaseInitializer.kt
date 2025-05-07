package dev.byjtech.erp.config.database

import dev.byjtech.erp.core.database.CoreTables
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseInitializer (private val database: Database) {

    fun initialize(vararg tables: Table) {
        transaction(database) {
            SchemaUtils.create(
                *tables
                //*CoreTables.all.toTypedArray()
            )
        }
    }

    fun nuke() {
        transaction(database) {
            SchemaUtils.drop(
                *CoreTables.all.reversed().toTypedArray()
            )
        }
    }

}