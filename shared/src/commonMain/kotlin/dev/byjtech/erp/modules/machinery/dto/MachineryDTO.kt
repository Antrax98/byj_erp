package dev.byjtech.erp.modules.machinery.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MachineryDTO(
    val id: String,
    val name: String,
    val model: String? = null,
    val manufacturer: String? = null,
    val serialNumber: String? = null,
    val status: String,
    val location: String? = null,
    val description: String? = null,
    val purchaseDate: LocalDateTime? = null,
    val warrantyExpirationDate: LocalDateTime? = null,
    val lastMaintenanceDate: LocalDateTime? = null,
    val nextMaintenanceDate: LocalDateTime? = null,
    val companyId: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val isActive: Boolean = true
)
