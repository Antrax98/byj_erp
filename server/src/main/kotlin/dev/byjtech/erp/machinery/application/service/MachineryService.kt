package dev.byjtech.erp.machinery.application.service

import dev.byjtech.erp.machinery.domain.model.Machinery
import dev.byjtech.erp.machinery.domain.repository.MachineryRepository
import dev.byjtech.erp.modules.machinery.request.CreateMachineryRequest
import dev.byjtech.erp.modules.machinery.request.UpdateMachineryRequest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

class MachineryService(
    private val machineryRepository: MachineryRepository,
    private val machineryHistoryService: MachineryHistoryService? = null
) {
    init {
        println("MachineryService initialized with historyService: ${machineryHistoryService != null}")
    }
    fun getAllMachineries(): List<Machinery> {
        return machineryRepository.findAllActive() // Solo activas por defecto
    }

    fun getAllActiveMachineries(): List<Machinery> {
        return machineryRepository.findAllActive()
    }

    fun getAllInactiveMachineries(): List<Machinery> {
        return machineryRepository.findAllInactive()
    }

    fun getMachineryById(id: java.util.UUID): Machinery? {
        return machineryRepository.findById(id)
    }

    fun getMachineriesByCompanyId(companyId: java.util.UUID): List<Machinery> {
        return machineryRepository.findActiveByCompanyId(companyId) // Solo activas por defecto
    }

    fun getActiveMachineriesByCompanyId(companyId: java.util.UUID): List<Machinery> {
        return machineryRepository.findActiveByCompanyId(companyId)
    }

    fun getInactiveMachineriesByCompanyId(companyId: java.util.UUID): List<Machinery> {
        return machineryRepository.findInactiveByCompanyId(companyId)
    }

    fun createMachinery(request: CreateMachineryRequest, userId: UUID? = null): Machinery {
        // Verificar que el código no exista
        if (machineryRepository.existsWithCode(request.code)) {
            throw IllegalArgumentException("Machinery with code '${request.code}' already exists")
        }

        // Crear la maquinaria
        val machinery = Machinery(
            code = request.code,
            name = request.name,
            description = request.description,
            brand = request.brand,
            model = request.model,
            year = request.year,
            serialNumber = request.serialNumber,
            licensePlate = request.licensePlate,
            status = request.status,
            location = request.location,
            companyId = UUID.fromString(request.companyId)
        )

        val savedMachinery = machineryRepository.save(machinery)
        
        // Registrar en el historial si se proporciona userId
        userId?.let { 
            machineryHistoryService?.logMachineryCreation(savedMachinery, it)
        }

        return savedMachinery
    }

    fun updateMachinery(id: UUID, request: UpdateMachineryRequest, userId: UUID? = null): Machinery {
        val existingMachinery = machineryRepository.findById(id)
            ?: throw IllegalArgumentException("Machinery with id $id not found")

        val updatedMachinery = existingMachinery.copy(
            name = request.name,
            description = request.description,
            brand = request.brand,
            model = request.model,
            year = request.year,
            serialNumber = request.serialNumber,
            licensePlate = request.licensePlate,
            status = request.status,
            location = request.location,
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )

        val savedMachinery = machineryRepository.update(updatedMachinery)
        
        // Registrar cambios en el historial si se proporciona userId
        userId?.let { 
            machineryHistoryService?.logMachineryUpdate(id, it, existingMachinery, savedMachinery)
        }

        return savedMachinery
    }

    fun deactivateMachinery(id: UUID, userId: UUID? = null): Machinery {
        val machinery = machineryRepository.findById(id)
            ?: throw IllegalArgumentException("Machinery with id $id not found")

        if (!machinery.isActive) {
            throw IllegalArgumentException("Machinery is already inactive")
        }

        val deactivatedMachinery = machinery.deactivate().copy(
            status = "INACTIVE", // Cambiar también el status
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )

        val savedMachinery = machineryRepository.update(deactivatedMachinery)
        
        // Registrar desactivación en el historial si se proporciona userId
        userId?.let { 
            machineryHistoryService?.logMachineryDeactivation(id, it)
        }

        return savedMachinery
    }

    fun activateMachinery(id: UUID, userId: UUID? = null): Machinery {
        val machinery = machineryRepository.findById(id)
            ?: throw IllegalArgumentException("Machinery with id $id not found")

        if (machinery.isActive) {
            throw IllegalArgumentException("Machinery is already active")
        }

        val activatedMachinery = machinery.activate().copy(
            status = "ACTIVE", // Cambiar también el status
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )

        val savedMachinery = machineryRepository.update(activatedMachinery)
        
        // Registrar activación en el historial si se proporciona userId
        userId?.let { 
            machineryHistoryService?.logMachineryActivation(id, it)
        }

        return savedMachinery
    }
}
