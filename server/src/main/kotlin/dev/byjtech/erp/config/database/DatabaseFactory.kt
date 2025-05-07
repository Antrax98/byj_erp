package dev.byjtech.erp.config.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database

class DatabaseFactory {

    val dotenv = dotenv{
        ignoreIfMissing = false
    }

    private val jdbcUrlEnv = "jdbc:mysql://${dotenv["DB_HOST"]}:${dotenv["DB_PORT"]}/${dotenv["DB_NAME"]}"

    private val config = HikariConfig().apply {
        jdbcUrl = jdbcUrlEnv
        driverClassName = "com.mysql.cj.jdbc.Driver"
        username = dotenv["DB_USER"]
        password = dotenv["DB_PASSWORD"]
        //username = "root"
        //password = "rootpassword"
        isReadOnly = false
        maximumPoolSize = 10
        transactionIsolation = "TRANSACTION_SERIALIZABLE"
    }

    private val dataSource = HikariDataSource(config)

    val database = Database.connect(datasource = dataSource)
}