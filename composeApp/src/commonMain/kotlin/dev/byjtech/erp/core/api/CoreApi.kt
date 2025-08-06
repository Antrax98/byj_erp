package dev.byjtech.erp.core.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.get

class CoreApi(private val client: HttpClient) {
    suspend fun getMyType(): String {
        return try {
            val response = client.get("api/core/users/me/type"){
                expectSuccess = false
            }
            response.body()
        } catch (e: Exception) {
            ""
        }
    }
}