package dev.byjtech.erp.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ApiResponse<out T> {
    @Serializable
    @SerialName("success")
    data class Success<T>(val data: T) : ApiResponse<T>()
    @Serializable
    @SerialName("error")
    data class Error(val message: String, val code: String) : ApiResponse<Nothing>()
}
