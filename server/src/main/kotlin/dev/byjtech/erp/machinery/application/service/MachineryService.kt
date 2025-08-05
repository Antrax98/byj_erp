package dev.byjtech.erp.machinery.application.service

import dev.byjtech.erp.machinery.domain.model.Machinery
import dev.byjtech.erp.machinery.domain.repository.MachineryRepository
import dev.byjtech.erp.modules.machinery.request.CreateMachineryRequest
import java.util.UUID

class MachineryService(
    private val machineryRepository: MachineryRepository
) {
    fun getAllMachineries(): List<Machinery> {
        return machineryRepository.findAll()
    }

    fun getMachineryById(id: java.util.UUID): Machinery? {
        return machineryRepository.findById(id)
    }

    fun getMachineriesByCompanyId(companyId: java.util.UUID): List<Machinery> {
        return machineryRepository.findByCompanyId(companyId)
    }

    fun getActiveMachineriesByCompanyId(companyId: java.util.UUID): List<Machinery> {
        return machineryRepository.findActiveByCompanyId(companyId)
    }

    fun createMachinery(request: CreateMachineryRequest): Machinery {
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

        return machineryRepository.save(machinery)
    }
}
