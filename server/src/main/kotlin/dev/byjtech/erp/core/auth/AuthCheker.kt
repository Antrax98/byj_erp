package dev.byjtech.erp.core.auth

import dev.byjtech.erp.core.database.rolePermission.RolePermissionDataSource.Companion.userHasAnyPermission
import dev.byjtech.erp.core.database.superAdmins.SuperAdminDataSource.Companion.isSuperAdmin
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.core.infrastructure.exposed.entities.SessionEntity
import dev.byjtech.erp.core.database.userSessions.UserSessionsDataSource as USDS
import dev.byjtech.erp.core.session.AppSession
import io.ktor.http.HttpStatusCode
import kotlin.text.removePrefix
import kotlin.text.trim
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondText
import io.ktor.http.ContentType

//patron de como nombrar los permisos
// <module_name>:<module_version>:<category_name>:<permission_name>
// ejemplo: hr:v2:resources:delete
// tienen que ser minusculas y sin espacios y exactamente igual que en la base de datos
// si es un permiso de los principales (user add, user delete, etc...)
// seria asi: core:core:users:delete
// core:core reemplazan el modulo y la version ya que no existen
// o se podria crear un modulo de mentiras solo para contenerlos


//TODO: moverlo al AuthServiceContractImpl y usar services que a su vez usen repositories


suspend fun authenticateAndAuthorize(
    call: ApplicationCall,
    requiredAnyPermissions: List<PermissionKey>? = null,
    requiredAdmin: Boolean = false
): SessionEntity {
    // Validate configuration
    if (requiredAnyPermissions != null && requiredAdmin) {
        call.respondText(
            text = "Error, both RequiredPermissions and RequiredAdmin cannot be set.",
            status = HttpStatusCode.Conflict,
            contentType = ContentType.Text.Plain
        )
        throw IllegalStateException("Error, both RequiredPermissions and RequiredAdmin cannot be set.")
    }

    //si no se le da configuracion, deveria dejar pasar el request solo para que le entregue el session
//    if (requiredPermissions == null && requiredAdmin == null) {
//        call.respondText(
//            text = "Error in the configuration, no configuration is defined",
//            status = HttpStatusCode.NotAcceptable,
//            contentType = ContentType.Text.Plain
//        )
//        return null
//    }

    val authHeader = call.request.headers["Authentication"]
    val token = authHeader?.removePrefix("Bearer ")?.trim()
    val appSession = token?.let { AppSession.fromEncoded(it) }
    if (appSession == null) {
        call.respondText(
            text = "Unauthorized. Missing authorization.",
            status = HttpStatusCode.Unauthorized,
            contentType = ContentType.Text.Plain
        )
        throw IllegalStateException("Unauthorized. Missing authorization.")
    }

    val session = USDS.findSessionById(appSession.sessionId)
    if (session == null) {
        call.respondText(
            text = "Unauthorized. Invalid Session.",
            status = HttpStatusCode.Unauthorized,
            contentType = ContentType.Text.Plain
        )
        throw IllegalStateException("Unauthorized. Invalid Session.")
    }

    //TODO: aqui crear una funcion que chequee si el usuario tiene los permisos requeridos
    //dentro de los permisos que tienen sus roles asignados
    if (requiredAnyPermissions != null) {
        val hasAnyPermissions = userHasAnyPermission(session.user.id.value, requiredAnyPermissions)
        if (!hasAnyPermissions) {
            call.respondText(
                text = "Forbidden. Missing permissions.",
                status = HttpStatusCode.Forbidden,
                contentType = ContentType.Text.Plain
            )
            throw IllegalStateException("Forbidden. Missing permissions.")
        }
    }

    //TODO: aqui crear una funcion que chequee si el usuario es SuperAdmin dentro de la tabla SuperAdmins
    //de ser necesario se podria crear una funcion que solo maneje a los Superadmin separada de esta
    if (requiredAdmin) {
        val isAdmin = isSuperAdmin(session.user.id.value)
        if (!isAdmin) {
            call.respondText(
                text = "Forbidden. Missing SuperAdmin.",
                status = HttpStatusCode.Forbidden,
                contentType = ContentType.Text.Plain
            )
            throw IllegalStateException("Forbidden. Missing SuperAdmin.")
        }
    }

    return session
}


//inutil ahora, sise usa el PermissionKey completo, pero podria ser usado en otro lado (frontend???)
fun permissionParser(permission: String): PermissionData {
    val parts = permission.split(":")
    if (parts.size != 4) {
        throw IllegalArgumentException("Invalid permission format")
    }
    return PermissionData(
        module = parts[0],
        version = parts[1],
        category = parts[2],
        permission = parts[3]
    )
}

data class PermissionData(
    val module: String,
    val version: String,
    val category: String,
    val permission: String
)