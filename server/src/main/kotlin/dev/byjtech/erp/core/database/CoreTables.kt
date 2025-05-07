package dev.byjtech.erp.core.database

import dev.byjtech.erp.core.database.users.Users
import dev.byjtech.erp.core.database.companies.Companies
import dev.byjtech.erp.core.database.modules.Modules
import dev.byjtech.erp.core.database.companyModule.CompanyModule
import dev.byjtech.erp.core.database.categories.Categories
import dev.byjtech.erp.core.database.permissions.Permissions
import dev.byjtech.erp.core.database.roles.Roles
import dev.byjtech.erp.core.database.rolePermission.RolePermission
import dev.byjtech.erp.core.database.superAdmins.SuperAdmins
import dev.byjtech.erp.core.database.userRole.UserRole
import dev.byjtech.erp.core.database.userSessions.UserSessions
import org.jetbrains.exposed.sql.Table

object CoreTables {
    val all = listOf<Table>(
        Users,
        Companies,
        Modules,
        CompanyModule,
        Categories,
        Permissions,
        Roles,
        RolePermission,
        UserRole,
        UserSessions,
        SuperAdmins
    )
}