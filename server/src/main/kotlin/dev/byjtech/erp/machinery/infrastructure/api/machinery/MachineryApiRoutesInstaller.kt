package dev.byjtech.erp.machinery.infrastructure.api.machinery

import dev.byjtech.erp.machinery.application.service.MachineryService
import dev.byjtech.erp.machinery.infrastructure.extensions.toDTO
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import java.util.UUID

class MachineryApiRoutesInstaller(
    private val machineryService: MachineryService
) : RoutesInstaller {
    
    override fun Route.installRoutes() {
        // Rutas directas sin un nivel extra "/machinery" porque ya está en el prefijo del módulo
        
        // Ruta para obtener todas las maquinarias
        get("/all") {
            try {
                val machineries = machineryService.getAllMachineries()
                call.respond(HttpStatusCode.OK, machineries.toDTO())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to fetch machineries: ${e.message}"))
            }
        }
        
        // Ruta para obtener maquinaria por ID
        get("/{id}") {
            try {
                val id = call.parameters["id"]?.let { UUID.fromString(it) }
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid machinery ID"))
                    return@get
                }
                
                val machinery = machineryService.getMachineryById(id)
                if (machinery != null) {
                    call.respond(HttpStatusCode.OK, machinery.toDTO())
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Machinery not found"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to fetch machinery: ${e.message}"))
            }
        }
        
        // Ruta para obtener maquinarias por compañía
        get("/company/{companyId}") {
            try {
                val companyId = call.parameters["companyId"]?.let { UUID.fromString(it) }
                if (companyId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid company ID"))
                    return@get
                }
                
                val machineries = machineryService.getMachineriesByCompanyId(companyId)
                call.respond(HttpStatusCode.OK, machineries.toDTO())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to fetch company machineries: ${e.message}"))
            }
        }
        
        // Ruta para obtener maquinarias activas por compañía
        get("/company/{companyId}/active") {
            try {
                val companyId = call.parameters["companyId"]?.let { UUID.fromString(it) }
                if (companyId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid company ID"))
                    return@get
                }
                
                val machineries = machineryService.getActiveMachineriesByCompanyId(companyId)
                call.respond(HttpStatusCode.OK, machineries.toDTO())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to fetch active company machineries: ${e.message}"))
            }
        }
    }
}
