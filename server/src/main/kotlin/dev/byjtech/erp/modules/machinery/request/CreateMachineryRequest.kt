package dev.byjtech.erp.modules.machinery.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateMachineryRequest(
    val code: String,
    val name: String,
    val description: String? = null,
    val brand: String,
    val model: String,
    val year: Int? = null,
    val serialNumber: String? = null,
    val licensePlate: String? = null,
    val status: String,
    val location: String? = null,
    val companyId: String
)
