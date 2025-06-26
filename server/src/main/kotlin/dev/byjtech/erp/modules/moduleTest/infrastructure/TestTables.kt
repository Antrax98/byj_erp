package dev.byjtech.erp.modules.moduleTest.infrastructure

import dev.byjtech.erp.modules.moduleTest.infrastructure.tables.ModerationsTable
import org.jetbrains.exposed.sql.Table

object TestTables {
    val all = setOf<Table>(
        ModerationsTable
    )
}