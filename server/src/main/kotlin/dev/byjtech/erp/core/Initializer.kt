package dev.byjtech.erp.core

import dev.byjtech.erp.config.ModuleCategory
import dev.byjtech.erp.config.ModuleDefinition
import dev.byjtech.erp.core.database.CoreTables
import dev.byjtech.erp.core.routes.CoreRoutes
import io.ktor.server.routing.Route

object Core: ModuleDefinition{
    override val name = "core"
    override val version = "core"

//    object Users {
//        const val NAME = "users"
//        const val DESCRIPTION = "Manage users"
//        val Create = ModulePermission("create", "Create a new users",
//            Core.name, Core.version, NAME
//        )
//        val Read   = ModulePermission("view", "View an user",
//            Core.name, Core.version, NAME
//        )
//        val Update = ModulePermission("update", "Update an user",
//            Core.name, Core.version, NAME
//        )
//        val Delete = ModulePermission("delete", "Delete an user",
//            Core.name, Core.version, NAME
//        )
//    }
//
//    object Admin {
//        const val NAME = "admin"
//        const val DESCRIPTION = "Manage admin permissions"
//        val All = ModulePermission("all", "Does everything company level", Core.name, Core.version, NAME)
//    }

    val Users = CoreCorePermissions.Users
    val Admin = CoreCorePermissions.Admin

    //definicion para rellenar las tablas de permisos y categorias
    override val categories = listOf(
        ModuleCategory(
            name = Users.NAME,
            description = Users.DESCRIPTION,
            permissions = listOf(Users.Create,Users.Read,Users.Update,Users.Delete)
        ),
        ModuleCategory(
            name = Admin.NAME,
            description = Admin.DESCRIPTION,
            permissions = listOf(Admin.All)
        )
    )

    //asegurarse que las tablas estan el el orden correcto
    override val tables = CoreTables.all

    override fun installRoutes(route: Route) {
        route.CoreRoutes()
    }
}