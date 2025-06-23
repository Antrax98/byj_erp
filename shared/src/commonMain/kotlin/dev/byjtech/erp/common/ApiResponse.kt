package dev.byjtech.erp.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ApiResponse<out S,out E> {
    @Serializable
    @SerialName("success")//todo:quitar
    data class Success<out S>(val data: S) : ApiResponse<S, Nothing>()
    @Serializable
    @SerialName("error")//todo:quitar
    data class Error<out E>(val data: E, val code: String) : ApiResponse<Nothing, E>()
}
