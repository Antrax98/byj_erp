package dev.byjtech.erp.core.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorList(
    val errors: List<String>
)
