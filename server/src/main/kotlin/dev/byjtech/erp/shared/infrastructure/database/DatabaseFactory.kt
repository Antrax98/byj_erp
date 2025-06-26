package dev.byjtech.erp.shared.infrastructure.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

//class DatabaseFactory {
//
//    private val dotenv = dotenv{
//        ignoreIfMissing = false
//    }
//
//    private val jdbcUrlEnv = "jdbc:mysql://${dotenv["DB_HOST"]}:${dotenv["DB_PORT"]}/${dotenv["DB_NAME"]}"
//
//    private val config = HikariConfig().apply {
//        jdbcUrl = jdbcUrlEnv
//        driverClassName = "com.mysql.cj.jdbc.Driver"
//        username = dotenv["DB_USER"]
//        password = dotenv["DB_PASSWORD"]
//        isReadOnly = false
//        maximumPoolSize = 10
//        transactionIsolation = "TRANSACTION_SERIALIZABLE"
//    }
//
//    private val dataSource = HikariDataSource(config)
//
//    val database = Database.connect(datasource = dataSource)
//}

fun CreateDatabase(
    db_host: String,
    db_port: String,
    db_name: String,
    db_user: String,
    db_password: String
): Database {
    val jdbcUrlEnv = "jdbc:mysql://${db_host}:${db_port}/${db_name}"
    val config = HikariConfig().apply {
        jdbcUrl = jdbcUrlEnv
        driverClassName = "com.mysql.cj.jdbc.Driver"
        username = db_user
        password = db_password
        isReadOnly = false
        maximumPoolSize = 10
        transactionIsolation = "TRANSACTION_SERIALIZABLE"
    }
    val dataSource = HikariDataSource(config)
    return Database.connect(datasource = dataSource)
}

fun Database.createTables(
    tables: List<Table>,
    maxRounds: Int = 10
) {
    val remaining = tables.toMutableSet()
    var round = 0

    while (remaining.isNotEmpty() && round < maxRounds) {
        val iterator = remaining.iterator()
        var atLeastOneCreated = false

        while (iterator.hasNext()) {
            val table = iterator.next()
            try {
                transaction(this) {
                    SchemaUtils.create(table)
                }
                atLeastOneCreated = true
                iterator.remove()
                println("Tabla creada: ${table.tableName}")
            } catch (e: Exception) {
                println("Error al crear ${table.tableName} en ronda $round: ${e.message}")
                // Se continúa con la siguiente tabla
            }
        }

        if (!atLeastOneCreated) {
            throw IllegalStateException(
                "No se pudo crear ninguna tabla en la ronda $round. " +
                        "Revisa dependencias o errores en definiciones."
            )
        }

        round++
    }

    if (remaining.isNotEmpty()) {
        throw IllegalStateException(
            "No se pudieron crear todas las tablas después de $round rondas."
        )
    }

    println("Todas las tablas fueron creadas correctamente.")
}