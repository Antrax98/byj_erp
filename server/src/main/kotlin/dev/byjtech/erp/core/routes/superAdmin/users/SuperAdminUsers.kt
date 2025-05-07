package dev.byjtech.erp.core.routes.superAdmin.users

import dev.byjtech.erp.core.auth.authenticateAndAuthorize
import dev.byjtech.erp.core.database.users.UserDataSource
import dev.byjtech.erp.core.database.users.toDTO
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.superAdminUsers() {
    //route("/get")
    //NO COMENSAR UN GET CON / O SE ROMPE LA RUTA
    get ("{id}"){
        val session = authenticateAndAuthorize(call, requiredAdmin = true)
        val id = call.request.queryParameters["id"]
        //var user: UserEntity? = null
        if (id == null) {
            call.respondText("Missing id")
            return@get
        }else{
            val user = UserDataSource.findUserById(id.toInt())
            if (user == null) {
                call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
            } else {
                call.respond(user.toDTO())
            }
        }
    }
}