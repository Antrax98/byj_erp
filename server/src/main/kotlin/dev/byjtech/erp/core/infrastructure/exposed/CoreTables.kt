package dev.byjtech.erp.core.infrastructure.exposed

import org.jetbrains.exposed.sql.Table
import dev.byjtech.erp.core.infrastructure.exposed.tables.*

object CoreTables {
    val all = setOf<Table>(
        UsersTable,
        CompaniesTable,
        BillingsTable,
        ModulesTable,
        SubscriptionsTable,
        CategoriesTable,
        PermissionsTable,
        RolesTable,
        RolePermissionTable,
        UserRoleTable,
        SessionsTable,
        SuperAdminsTable,
        UserPermissionTable
    )
}