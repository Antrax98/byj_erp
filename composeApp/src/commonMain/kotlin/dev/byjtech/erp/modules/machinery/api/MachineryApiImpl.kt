package dev.byjtech.erp.modules.machinery.api

import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.modules.machinery.dto.MachineryDTO
import dev.byjtech.erp.modules.machinery.request.CreateMachineryRequest
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
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
}
