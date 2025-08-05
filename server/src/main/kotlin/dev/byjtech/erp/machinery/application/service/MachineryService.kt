package dev.byjtech.erp.machinery.application.service

import dev.byjtech.erp.machinery.domain.model.Machinery
import dev.byjtech.erp.machinery.domain.repository.MachineryRepository

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
}
