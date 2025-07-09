package dev.byjtech.erp.document_management.infrastructure.exposed

import dev.byjtech.erp.document_management.infrastructure.exposed.tables.*
import org.jetbrains.exposed.sql.Table

object DocumentManagementTables {
    val all = setOf<Table>(
        DocumentsTable,
        DocumentEditHistoryTable,
        DocumentAuditLogTable
    )
}
