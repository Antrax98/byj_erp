package dev.byjtech.erp.core.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateCompanyRequest(
    val adminEmail: String,
    val adminName: String,
    val companyName: String,
    val companyRut: String,
    val companyContactEmail: String
)
