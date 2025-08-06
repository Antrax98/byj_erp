package dev.byjtech.erp.machinery.infrastructure.api.machinery

import dev.byjtech.erp.machinery.application.service.MachineryService
import dev.byjtech.erp.machinery.application.service.MachineryHistoryService
import dev.byjtech.erp.machinery.infrastructure.extensions.toDTO
import dev.byjtech.erp.modules.machinery.request.CreateMachineryRequest
import dev.byjtech.erp.modules.machinery.request.UpdateMachineryRequest
import dev.byjtech.erp.core.infrastructure.auth.CoreAuthWrapper
import dev.byjtech.erp.shared.routing.RoutesInstaller
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import java.util.UUID

class MachineryApiRoutesInstaller(
    private val machineryService: MachineryService,
    private val machineryHistoryService: MachineryHistoryService,
    private val authServ: CoreAuthWrapper
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
        
        // Ruta para crear una nueva maquinaria
        post("/create") {
            try {
                // Autenticación opcional para el historial
                val session = try {
                    authServ.authorizeOrThrow(call)
                } catch (e: Exception) {
                    null // Si no hay autenticación, continúa sin historial
                }
                
                val request = call.receive<CreateMachineryRequest>()
                val createdMachinery = machineryService.createMachinery(request, session?.userId)
                call.respond(HttpStatusCode.Created, createdMachinery.toDTO())
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to create machinery: ${e.message}"))
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
        
        // Ruta para obtener maquinarias inactivas/desactivadas
        get("/inactive") {
            try {
                val machineries = machineryService.getAllInactiveMachineries()
                call.respond(HttpStatusCode.OK, machineries.toDTO())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to fetch inactive machineries: ${e.message}"))
            }
        }
        
        // Ruta para actualizar una maquinaria
        put("/{id}") {
            try {
                // Autenticación opcional para el historial
                val session = try {
                    authServ.authorizeOrThrow(call)
                } catch (e: Exception) {
                    null // Si no hay autenticación, continúa sin historial
                }
                
                val id = call.parameters["id"]?.let { UUID.fromString(it) }
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid machinery ID"))
                    return@put
                }
                
                val request = call.receive<UpdateMachineryRequest>()
                
                // TEMPORAL: Para testing del historial
                val testUserId = session?.userId ?: UUID.fromString("00000000-0000-0000-0000-000000000001")
                
                val updatedMachinery = machineryService.updateMachinery(id, request, testUserId)
                call.respond(HttpStatusCode.OK, updatedMachinery.toDTO())
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to update machinery: ${e.message}"))
            }
        }
        
        // Ruta para desactivar una maquinaria
        patch("/{id}/deactivate") {
            try {
                // Autenticación opcional para el historial
                val session = try {
                    authServ.authorizeOrThrow(call)
                } catch (e: Exception) {
                    null // Si no hay autenticación, continúa sin historial
                }
                
                val id = call.parameters["id"]?.let { UUID.fromString(it) }
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid machinery ID"))
                    return@patch
                }
                
                val deactivatedMachinery = machineryService.deactivateMachinery(id, session?.userId)
                call.respond(HttpStatusCode.OK, deactivatedMachinery.toDTO())
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to deactivate machinery: ${e.message}"))
            }
        }
        
        // Ruta para reactivar una maquinaria
        patch("/{id}/activate") {
            try {
                // Autenticación opcional para el historial
                val session = try {
                    authServ.authorizeOrThrow(call)
                } catch (e: Exception) {
                    null // Si no hay autenticación, continúa sin historial
                }
                
                val id = call.parameters["id"]?.let { UUID.fromString(it) }
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid machinery ID"))
                    return@patch
                }
                
                val activatedMachinery = machineryService.activateMachinery(id, session?.userId)
                call.respond(HttpStatusCode.OK, activatedMachinery.toDTO())
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to activate machinery: ${e.message}"))
            }
        }
        
        // Ruta para obtener maquinarias inactivas por compañía
        get("/company/{companyId}/inactive") {
            try {
                val companyId = call.parameters["companyId"]?.let { UUID.fromString(it) }
                if (companyId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid company ID"))
                    return@get
                }
                
                val machineries = machineryService.getInactiveMachineriesByCompanyId(companyId)
                call.respond(HttpStatusCode.OK, machineries.toDTO())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to fetch inactive company machineries: ${e.message}"))
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
        
        // ===== RUTAS DEL HISTORIAL DE MAQUINARIA =====
        
        // Ruta para obtener el historial de una maquinaria específica
        get("/{id}/history") {
            try {
                val machineryId = call.parameters["id"]?.let { UUID.fromString(it) }
                if (machineryId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid machinery ID"))
                    return@get
                }
                
                val history = machineryHistoryService.getHistoryByMachineryId(machineryId)
                call.respond(HttpStatusCode.OK, history.map { it.toDTO() })
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to fetch machinery history: ${e.message}"))
            }
        }
        
        // Ruta para obtener el historial de cambios por usuario
        get("/history/user/{userId}") {
            try {
                // Autenticación requerida
                val session = authServ.authorizeOrThrow(call)
                
                val userId = call.parameters["userId"]?.let { UUID.fromString(it) }
                if (userId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid user ID"))
                    return@get
                }
                
                // Verificar que el usuario puede ver este historial (por ejemplo, solo sus propios cambios o admin)
                val history = machineryHistoryService.getHistoryByUserId(userId)
                call.respond(HttpStatusCode.OK, history.map { it.toDTO() })
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to fetch user history: ${e.message}"))
            }
        }
        
        // Ruta para agregar una entrada personalizada al historial (para mantenimiento, etc.)
        post("/{id}/history") {
            try {
                // Autenticación requerida
                val session = authServ.authorizeOrThrow(call)
                
                val id = call.parameters["id"]?.let { UUID.fromString(it) }
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid machinery ID"))
                    return@post
                }
                
                val request = call.receive<Map<String, String?>>()
                val field = request["field"] ?: throw IllegalArgumentException("Field is required")
                val oldValue = request["oldValue"]
                val newValue = request["newValue"]
                val comment = request["comment"]
                
                val historyEntry = machineryHistoryService.addCustomHistoryEntry(
                    machineryId = id,
                    userId = session.userId,
                    field = field,
                    oldValue = oldValue,
                    newValue = newValue,
                    comment = comment
                )
                
                call.respond(HttpStatusCode.Created, historyEntry.toDTO())
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to add history entry: ${e.message}"))
            }
        }
        
        // Ruta para marcar inicio de mantenimiento
        post("/{id}/maintenance/start") {
            try {
                // Autenticación requerida
                val session = authServ.authorizeOrThrow(call)
                
                val id = call.parameters["id"]?.let { UUID.fromString(it) }
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid machinery ID"))
                    return@post
                }
                
                val request = call.receive<Map<String, String?>>()
                val comment = request["comment"]
                
                val historyEntry = machineryHistoryService.logMaintenanceStart(id, session.userId, comment)
                call.respond(HttpStatusCode.Created, historyEntry.toDTO())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to log maintenance start: ${e.message}"))
            }
        }
        
        // Ruta para marcar fin de mantenimiento
        post("/{id}/maintenance/end") {
            try {
                // Autenticación requerida
                val session = authServ.authorizeOrThrow(call)
                
                val id = call.parameters["id"]?.let { UUID.fromString(it) }
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid machinery ID"))
                    return@post
                }
                
                val request = call.receive<Map<String, String?>>()
                val comment = request["comment"]
                
                val historyEntry = machineryHistoryService.logMaintenanceEnd(id, session.userId, comment)
                call.respond(HttpStatusCode.Created, historyEntry.toDTO())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to log maintenance end: ${e.message}"))
            }
        }
    }
}
