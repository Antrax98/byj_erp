package dev.byjtech.erp.modules.machinery.api

import dev.byjtech.erp.modules.machinery.dto.MachineryDTO
import dev.byjtech.erp.modules.machinery.dto.MachineryHistoryDTO
import dev.byjtech.erp.modules.machinery.request.CreateMachineryRequest
import dev.byjtech.erp.modules.machinery.request.UpdateMachineryRequest

interface MachineryApi {
    suspend fun getAllMachinery(): Result<List<MachineryDTO>>
    suspend fun getInactiveMachinery(): Result<List<MachineryDTO>>
    suspend fun getMachineryById(id: String): Result<MachineryDTO?>
    suspend fun getMachineriesByCompany(companyId: String): Result<List<MachineryDTO>>
    suspend fun createMachinery(request: CreateMachineryRequest): Result<MachineryDTO>
    suspend fun updateMachinery(id: String, request: UpdateMachineryRequest): Result<MachineryDTO>
    suspend fun deactivateMachinery(id: String): Result<MachineryDTO>
    suspend fun activateMachinery(id: String): Result<MachineryDTO>
    suspend fun getMachineryHistory(machineryId: String): Result<List<MachineryHistoryDTO>>
}
