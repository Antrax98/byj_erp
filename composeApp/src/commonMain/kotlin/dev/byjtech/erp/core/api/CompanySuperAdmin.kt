package dev.byjtech.erp.core.api

import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.core.dto.CompanyDTO
import dev.byjtech.erp.core.request.CreateCompanyRequest
import dev.byjtech.erp.core.response.ErrorList
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class CompanySuperAdmin(private val client: HttpClient) {

    suspend fun getAllCompanies(): Set<CompanyDTO> {
        return try {
            client.get("api/core/companies/super-admin/all-companies").body()
        } catch (e: Exception) {
            emptySet()
        }
    }
    //TODO(): usar ApiResponse en los que no lo usen
    suspend fun createCompany(data: CreateCompanyRequest): ApiResponse<Unit,ErrorList?> {
        return try {
            val response = client.post("api/core/companies/super-admin/create-company") {
                contentType(ContentType.Application.Json)
                setBody(data)
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    println("OK")
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.Conflict -> {
                    val requestBody = response.body<ErrorList>()
                    println("CONFLICT_REQUEST")
                    ApiResponse.Error(requestBody, "CONFLICT_REQUEST")
                }
                HttpStatusCode.BadRequest -> {
                    println("BAD_REQUEST")
                    ApiResponse.Error(null, "BAD_REQUEST")
                }
                else -> {
                    println("UNKNOWN_REQUEST_ERROR")
                    ApiResponse.Error(null, "UNKNOWN_REQUEST_ERROR")
                }
                //TODO(): agregar mas errores
            }
        } catch (e: Exception) {
            println(e)
            ApiResponse.Error(null, "NETWORK_ERROR")
        }
    }

    suspend fun updateCompanyName(companyId: String, newName: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.patch("api/core/companies/super-admin/update-company-name/$companyId/$newName"){
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")

        }
    }

    suspend fun updateCompanyEmail(companyId: String, newEmail: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.patch("api/core/companies/super-admin/update-company-contact-email/$companyId/$newEmail"){
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception) {
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

}