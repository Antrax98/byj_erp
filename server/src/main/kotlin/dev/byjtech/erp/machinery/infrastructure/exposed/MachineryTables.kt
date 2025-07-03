package dev.byjtech.erp.machinery.infrastructure.exposed

import org.jetbrains.exposed.sql.Table
import dev.byjtech.erp.machinery.infrastructure.exposed.tables.*

object MachineryTables {
    val all = setOf<Table>(
        MachineriesTable,
        MachineryHistoriesTable,
        MachineryDocumentsTable,
        MaintenanceSchedulesTable,
        MaintenanceActivitiesTable,
        WorkOrdersTable,
        WorkOrderActivitiesTable,
        OperationalDataTable,
        MachineryNotificationsTable
    )
}
