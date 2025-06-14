package dev.byjtech.erp.core.infrastructure.api.users

import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toDTO
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.superAdminUsers(authServ: CoreAuthWrapper, userRepo: UserRepository) {
    get ("{id}"){
        val session = authServ.authorizeOrThrow(call, requiredSuperAdmin = true)
        val id = call.request.queryParameters["id"]
        //var user: UserEntity? = null
        if (id == null) {
            call.respondText("Missing id")
            return@get
        }else{
            val user = userRepo.find(id.toInt())
            if (user == null) {
                call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
            } else {
                call.respond(user.toDTO())
            }
        }
    }
}