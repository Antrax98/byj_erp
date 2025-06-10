package dev.byjtech.erp.common.api

import kotlinx.serialization.Serializable

@Serializable
sealed class ApiResponse<out T> {
    @Serializable
    data class Success<T>(val data: T) : ApiResponse<T>()
    @Serializable
    data class Error(val message: String, val code: String) : ApiResponse<Nothing>()
}
