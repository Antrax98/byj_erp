package dev.byjtech.erp.modules.announcements.v1

import dev.byjtech.erp.config.ModuleCategory
import dev.byjtech.erp.config.ModuleDefinition
import dev.byjtech.erp.modules.announcements.v1.database.AnnouncementsV1Tables
import dev.byjtech.erp.modules.announcements.v1.routes.announcementsV1Routes
import io.ktor.server.routing.Route

object AnnouncementsV1 : ModuleDefinition {
    override val name = AnnouncementsV1Permissions.name
    override val version = AnnouncementsV1Permissions.version

    //definicion para poder obtener los permisos de este modulo para las rutas
    //por cada categoria crear un objeto con los permisos correspondientes y agregarlo al categories listOf
//    object Misc {
//        const val NAME = "misc"
//        const val DESCRIPTION = "Manage announcements"
//        val Create = ModulePermission("create", "Create a new announcement",
//            name, version, NAME
//        )
//        val Read = ModulePermission("read", "Read an announcement",
//            name, version, NAME
//        )
//        val Edit = ModulePermission("edit", "Update an announcement",
//            name, version, NAME
//        )
//        val Delete = ModulePermission("delete", "Delete an announcement",
//            name, version, NAME
//        )
//        val Pin = ModulePermission("pin", "Pin an announcement", name, version, NAME)
//    }
    val Misc = AnnouncementsV1Permissions.Misc

    //definicion para rellenar las tablas de permisos y categorias
    override val categories = listOf(
        ModuleCategory(
            name = Misc.NAME,
            description = Misc.DESCRIPTION,
            permissions = listOf(Misc.Create,Misc.Read,Misc.Edit,Misc.Delete,Misc.Pin)
        )
    )

    // aqui van las tablas de este modulo
    override val tables = AnnouncementsV1Tables.all



    override fun installRoutes(route: Route) {
        route.announcementsV1Routes()
    }

}