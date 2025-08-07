package dev.byjtech.erp.core.api

import dev.byjtech.erp.common.ApiResponse
import dev.byjtech.erp.core.dto.ModuleDTO
import dev.byjtech.erp.core.response.SubscriptionsModResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

class SubscriptionsSuperAdmin(private val client: HttpClient) {
    suspend fun getCompanySubscriptions(companyId: String): ApiResponse<Set<SubscriptionsModResponse>, Unit> {
        return try{
            val response = client.get("api/core/subscriptions/super-admin/company-subscriptions/$companyId"){
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<Set<SubscriptionsModResponse>>()
                    ApiResponse.Success(data)
                }
                //"NO_COMPANY_ID"
                HttpStatusCode.BadRequest -> {
                    val errorMessage = response.bodyAsText()
                    ApiResponse.Error(Unit, errorMessage)
                }
                else -> {
                    ApiResponse.Error(Unit, "UNKNOWN_REQUEST_ERROR")
                }
            }
        } catch (e: Exception){
            ApiResponse.Error(Unit, "NETWORK_ERROR")
        }
    }

    suspend fun getAllModules(): ApiResponse<Set<ModuleDTO>,Unit> {
        return try {
            val response = client.get("api/core/subscriptions/super-admin/possible-modules"){
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<Set<ModuleDTO>>()
                    ApiResponse.Success(data)
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

    suspend fun createSubscription(companyId: String, moduleId: String): ApiResponse<Unit, Unit> {
        return try {
            val response = client.post("api/core/subscriptions/super-admin/assign-module-to-company/$companyId/$moduleId"){
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK ->
                    ApiResponse.Success(Unit)
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

    suspend fun changeSubscriptionAccess(subscriptionId: String, isAccessible: Boolean): ApiResponse<Unit, Unit>{
        return try {
            val response = client.patch("api/core/subscriptions/super-admin/update-access-status/$subscriptionId/$isAccessible"){
                expectSuccess = false
            }
            when (response.status) {
                HttpStatusCode.OK ->
                    ApiResponse.Success(Unit)
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