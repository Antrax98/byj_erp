package dev.byjtech.erp.modules.announcements.v1.routes

import dev.byjtech.erp.modules.announcements.v1.routes.superAdmin.superAdminRoutes
import dev.byjtech.erp.modules.announcements.v1.routes.tenant.tenantRoutes
import io.ktor.server.routing.*

//este modulo en particular no se subdivide, mas que para el superAdmin y tenant, ya que es muy simple
fun Route.announcementsV1Routes(){
    route("/announcements/v1"){
        route("/superadmin"){
            superAdminRoutes()
        }
        route("/tenant"){
            tenantRoutes()
        }
    }
}