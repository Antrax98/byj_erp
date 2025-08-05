package dev.byjtech.erp.modules.machinery.api

import dev.byjtech.erp.modules.machinery.dto.MachineryDTO
import dev.byjtech.erp.modules.machinery.request.CreateMachineryRequest

interface MachineryApi {
    suspend fun getAllMachinery(): Result<List<MachineryDTO>>
    suspend fun getMachineryById(id: String): Result<MachineryDTO?>
    suspend fun getMachineriesByCompany(companyId: String): Result<List<MachineryDTO>>
    suspend fun createMachinery(request: CreateMachineryRequest): Result<MachineryDTO>
}
