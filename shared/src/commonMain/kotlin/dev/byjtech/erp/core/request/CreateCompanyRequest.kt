package dev.byjtech.erp.core.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateCompanyRequest(
    val adminEmail: String,
    val name: String,
    val rut: String,
    val contactEmail: String
)
