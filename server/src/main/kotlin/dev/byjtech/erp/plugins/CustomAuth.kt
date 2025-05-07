package dev.byjtech.erp.plugins

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!DEPRECATED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

//import dev.byjtech.erp.core.database.userSessions.UserSessionEntity
//import dev.byjtech.erp.core.database.userSessions.UserSessionsDataSource
//import dev.byjtech.erp.core.session.AppSession
//import io.ktor.http.ContentType
//import io.ktor.http.HttpStatusCode
//import io.ktor.server.application.ApplicationCall
//import io.ktor.server.application.call
//import io.ktor.server.application.createApplicationPlugin
//import io.ktor.server.response.respondText
//import io.ktor.util.AttributeKey
//import io.ktor.util.pipeline.PipelineContext

//val RequiredPermissionsKey = AttributeKey<List<String>>("RequiredPermissions")
//val RequiredAdminKey = AttributeKey<Boolean>("RequiredPermissions")
//
//// Plugin para chekear permisos tanto para permisos de modulo como de SuperAdmin
//val AuthenticatedPlugin = createApplicationPlugin(
//    name = "AuthenticatedPlugin"
//) {
//    onCall { call ->
//
//        val requiredPermissions = call.attributes.getOrNull(RequiredPermissionsKey)
//        val requiredAdmin = call.attributes.getOrNull(RequiredAdminKey)
//
//        //si los dos atributos estan puestos, devolver error
//        if(!validateConfiguration(call)) return@onCall
//
//        //si no hay valores para chequear, dejara pasar
//        if(requiredPermissions == null && requiredAdmin == null) {
//            call.respondText(
//                text = "OOOOOOOOOOPSSSS",
//                status = HttpStatusCode.NotAcceptable,
//                contentType = ContentType.Text.Plain
//            )
//            return@onCall
//        }
//
//        val authHeader = call.request.headers["Authentication"]
//        val token = authHeader?.removePrefix("Bearer ")?.trim()
//        val appSession = token?.let { AppSession.fromEncoded(it) }
//
//        if (appSession == null) {
//            call.respondText(
//                text = "Unauthorized. Missing authorization.",
//                status = HttpStatusCode.Unauthorized,
//                contentType = ContentType.Text.Plain
//            )
//            return@onCall
//        }
//
//        val session = UserSessionsDataSource.findSessionById(appSession.userSessionEntityId)
//        if (session == null) {
//            call.respondText(
//                text = "Unauthorized. Invalid appSession.",
//                status = HttpStatusCode.Unauthorized,
//                contentType = ContentType.Text.Plain
//            )
//            return@onCall
//        }
//
//        if(requiredPermissions != null){
//
//            val hasAllPermissions = true
//            //val hasAllPermissions = session.permissions.containsAll(requiredPermissions.toList())
//
//            if (!hasAllPermissions) {
//                call.respondText(
//                    text = "Forbidden. Missing permissions.",
//                    status = HttpStatusCode.Forbidden,
//                    contentType = ContentType.Text.Plain
//                )
//                return@onCall
//            }
//            // The session is saved in a call attribute
//
//        }
//
//        if(requiredAdmin != null){
//            //dejarlo pasar por ahora
//
//            //usar la session para obtener el user y revisar la tabla de SuperAdmin si es que este user esta referenciado
//        }
//
//        call.attributes.put(sessionKey,session)
//    }
//}
//
//// Add an attribute for the UserSession
//val sessionKey = AttributeKey<UserSessionEntity>("UserSession")
//
//// Function to get the UserSession in the handler // no sirve????
////fun PipelineContext<Unit, ApplicationCall>.getUserSession(): UserSessionEntity {
////    return call.attributes[sessionKey]
////}
//
//suspend fun validateConfiguration(call: ApplicationCall): Boolean {
//    val permissions = call.attributes.getOrNull(RequiredPermissionsKey)
//    val admin = call.attributes.getOrNull(RequiredAdminKey)
//
//    return if (permissions != null && admin != null) {
//        call.respondText(
//            text = "Error, both RequiredPermissions and RequiredAdmin cannot be set.",
//            status = HttpStatusCode.Conflict,
//            contentType = ContentType.Text.Plain
//        )
//        false
//    } else true
//}