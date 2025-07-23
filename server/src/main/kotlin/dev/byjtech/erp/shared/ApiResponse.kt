package dev.byjtech.erp.shared

import kotlinx.serialization.Serializable

//¡¡¡¡¡¡¡¡¡¡¡NO USAR!!!!!!!!!!!!
@Serializable
sealed class ApiResponse<out T> {
    @Serializable
    data class Success<T>(val data: T) : ApiResponse<T>()
    @Serializable
    data class Error(val message: String, val code: String) : ApiResponse<Nothing>()
}