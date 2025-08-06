package dev.byjtech.erp.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//Solo usar en el frontend si se quiere
@Serializable
sealed class ApiResponse<out S,out E> {
    @Serializable
    @SerialName("success")
    data class Success<out S>(val data: S) : ApiResponse<S, Nothing>()
    @Serializable
    @SerialName("error")
    data class Error<out E>(val data: E, val code: String) : ApiResponse<Nothing, E>()
}
