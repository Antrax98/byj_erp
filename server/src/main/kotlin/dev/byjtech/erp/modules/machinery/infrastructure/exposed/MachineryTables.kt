package dev.byjtech.erp.modules.machinery.infrastructure.exposed

import org.jetbrains.exposed.sql.Table
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.*

object MachineryTables {
    val all = setOf<Table>(
        MachineriesTable,
        MachineryHistoriesTable,
        dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable,
        MaintenanceSchedulesTable,
        MaintenanceActivitiesTable,
        WorkOrdersTable,
        WorkOrderActivitiesTable,
        OperationalDataTable,
        MachineryNotificationsTable
    )
}
