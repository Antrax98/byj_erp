package dev.byjtech.erp.modules.machinery.api

import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.modules.machinery.dto.MachineryDTO
import dev.byjtech.erp.modules.machinery.dto.MachineryHistoryDTO
import dev.byjtech.erp.modules.machinery.request.CreateMachineryRequest
import dev.byjtech.erp.modules.machinery.request.UpdateMachineryRequest
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class MachineryApiImpl(
    private val apiClient: ApiClient
) : MachineryApi {
    
    override suspend fun getAllMachinery(): Result<List<MachineryDTO>> {
        return try {
            val response = apiClient.clientKtor.get("/api/machinery/all")
            
            if (response.status == HttpStatusCode.OK) {
                val machineries = response.body<List<MachineryDTO>>()
                Result.success(machineries)
            } else {
                Result.failure(Exception("Failed to fetch machineries: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getMachineryById(id: String): Result<MachineryDTO?> {
        return try {
            val response = apiClient.clientKtor.get("/api/machinery/$id")
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val machinery = response.body<MachineryDTO>()
                    Result.success(machinery)
                }
                HttpStatusCode.NotFound -> Result.success(null)
                else -> Result.failure(Exception("Failed to fetch machinery: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getMachineriesByCompany(companyId: String): Result<List<MachineryDTO>> {
        return try {
            val response = apiClient.clientKtor.get("/api/machinery/company/$companyId")
            
            if (response.status == HttpStatusCode.OK) {
                val machineries = response.body<List<MachineryDTO>>()
                Result.success(machineries)
            } else {
                Result.failure(Exception("Failed to fetch company machineries: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun createMachinery(request: CreateMachineryRequest): Result<MachineryDTO> {
        return try {
            val response = apiClient.clientKtor.post("/api/machinery/create") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            
            when (response.status) {
                HttpStatusCode.Created -> {
                    val machinery = response.body<MachineryDTO>()
                    Result.success(machinery)
                }
                HttpStatusCode.BadRequest -> {
                    val errorResponse = response.body<Map<String, String>>()
                    Result.failure(Exception(errorResponse["error"] ?: "Bad request"))
                }
                else -> {
                    Result.failure(Exception("Failed to create machinery: ${response.status}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getInactiveMachinery(): Result<List<MachineryDTO>> {
        return try {
            val response = apiClient.clientKtor.get("/api/machinery/inactive")
            
            if (response.status == HttpStatusCode.OK) {
                val machineries = response.body<List<MachineryDTO>>()
                Result.success(machineries)
            } else {
                Result.failure(Exception("Failed to fetch inactive machineries: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateMachinery(id: String, request: UpdateMachineryRequest): Result<MachineryDTO> {
        return try {
            val response = apiClient.clientKtor.put("/api/machinery/$id") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val machinery = response.body<MachineryDTO>()
                    Result.success(machinery)
                }
                HttpStatusCode.BadRequest -> {
                    val errorResponse = response.body<Map<String, String>>()
                    Result.failure(Exception(errorResponse["error"] ?: "Bad request"))
                }
                else -> {
                    Result.failure(Exception("Failed to update machinery: ${response.status}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deactivateMachinery(id: String): Result<MachineryDTO> {
        return try {
            val response = apiClient.clientKtor.patch("/api/machinery/$id/deactivate")
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val machinery = response.body<MachineryDTO>()
                    Result.success(machinery)
                }
                HttpStatusCode.BadRequest -> {
                    val errorResponse = response.body<Map<String, String>>()
                    Result.failure(Exception(errorResponse["error"] ?: "Bad request"))
                }
                else -> {
                    Result.failure(Exception("Failed to deactivate machinery: ${response.status}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun activateMachinery(id: String): Result<MachineryDTO> {
        return try {
            val response = apiClient.clientKtor.patch("/api/machinery/$id/activate")
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val machinery = response.body<MachineryDTO>()
                    Result.success(machinery)
                }
                HttpStatusCode.BadRequest -> {
                    val errorResponse = response.body<Map<String, String>>()
                    Result.failure(Exception(errorResponse["error"] ?: "Bad request"))
                }
                else -> {
                    Result.failure(Exception("Failed to activate machinery: ${response.status}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getMachineryHistory(machineryId: String): Result<List<MachineryHistoryDTO>> {
        return try {
            val response = apiClient.clientKtor.get("/api/machinery/$machineryId/history")
            
            if (response.status == HttpStatusCode.OK) {
                val historyList = response.body<List<MachineryHistoryDTO>>()
                Result.success(historyList)
            } else {
                Result.failure(Exception("Failed to fetch machinery history: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
