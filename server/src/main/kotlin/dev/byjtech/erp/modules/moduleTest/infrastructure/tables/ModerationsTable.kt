package dev.byjtech.erp.modules.moduleTest.infrastructure.tables

import org.jetbrains.exposed.dao.id.UUIDTable

object ModerationsTable : UUIDTable("moderations") {
    val description = text("description")
}