package dev.byjtech.erp.modules.document_management.infrastructure.exposed

import dev.byjtech.erp.modules.document_management.infrastructure.exposed.tables.*
import org.jetbrains.exposed.sql.Table

object DocumentManagementTables {
    val all = setOf<Table>(
        DocumentsTable,
        CreditNotesTable,
        DocumentEditHistoryTable,
        DocumentAuditLogTable
    )
}
